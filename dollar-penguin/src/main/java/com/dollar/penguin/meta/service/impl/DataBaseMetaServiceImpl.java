package com.dollar.penguin.meta.service.impl;

import com.alibaba.excel.EasyExcel;
import com.dollar.penguin.common.DataException;
import com.dollar.penguin.common.ExcelService;
import com.dollar.penguin.meta.mapper.MetaMapper;
import com.dollar.penguin.meta.model.DataBaseConfigTemplatePojo;
import com.dollar.penguin.meta.model.DataBaseEntity;
import com.dollar.penguin.meta.model.vo.DataBaseVo;
import com.dollar.penguin.meta.service.DataBaseConfigDataListener;
import com.dollar.penguin.meta.service.DataBaseMetaService;
import com.dollar.penguin.util.TEAUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.penguin.database.util.DBType;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class DataBaseMetaServiceImpl implements DataBaseMetaService {

    @Autowired
    private MetaMapper metaMapper;
    @Resource
    private ExcelService excelService;

    @Override
    public PageInfo<DataBaseEntity> queryDataBase(String code, String name, int iDisplayStart,
            int iDisplayLength) {
        // 使用 PageHelper 进行分页
        int pageNum = (iDisplayStart / iDisplayLength) + 1; // 计算当前页
        PageHelper.startPage(pageNum, iDisplayLength);
        // 执行查询
        List<DataBaseEntity> dataList = metaMapper.queryDataBase(code, name);
        if (dataList.isEmpty()) {
            throw new DataException(DataException.DATA_SELECT_FAILED, "DataBase query is empty!");
        }
        // 封装成 PageInfo 对象并返回
        return new PageInfo<>(dataList);
    }

    @Override
    public boolean insertDataBaseInformation(DataBaseVo dataBaseVo) {
        // 数据验证
        checkData(dataBaseVo);
        // 插入数据
        DataBaseEntity dataBaseEntity = buildDataBaseEntity(dataBaseVo);
        int rsCount = metaMapper.insertDataBase(dataBaseEntity);
        if (rsCount != 1) {
            throw new DataException(DataException.DATA_INSERT_FAILED, "DataBase insert exception!");
        }
        return true;
    }

    private void checkData(DataBaseVo dataBaseVo) {
        if (StringUtils.isEmpty(dataBaseVo.getUrl())) {
            throw new DataException(DataException.DATA_BASE_PARAMETER_FAILED, "URL Is Null!");
        }
        if (StringUtils.isEmpty(dataBaseVo.getPwd())) {
            throw new DataException(DataException.DATA_BASE_PARAMETER_FAILED, "Password Is Null!");
        }
        if (StringUtils.isEmpty(dataBaseVo.getUserName())) {
            throw new DataException(DataException.DATA_BASE_PARAMETER_FAILED, "UserName Is Null!");
        }
    }

    private DataBaseEntity buildDataBaseEntity(DataBaseVo dataBaseVo) {
        DataBaseEntity dataBaseEntity = new DataBaseEntity();
        DBType dbType;
        if (StringUtils.isEmpty(dataBaseVo.getDataBaseName())) {
            dbType = DBType.getDBType(dataBaseVo.getUrl());
        } else {
            dbType = DBType.recognizeDbType(dataBaseVo.getDataBaseName());
            dataBaseEntity.setDataBaseName(dataBaseVo.getDataBaseName());
        }
        dataBaseEntity.setDataBaseCode(dbType.getIndex());
        dataBaseEntity.setDataBaseName(dbType.name());
        dataBaseEntity.setUrl(dataBaseVo.getUrl());
        // 密码加密
        String pwd = TEAUtil.encode(dataBaseVo.getPwd());
        dataBaseEntity.setPwd(pwd);
        dataBaseEntity.setUserName(dataBaseVo.getUserName());
        return dataBaseEntity;
    }

    @Override
    public void download(HttpServletResponse response) throws IOException {
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            String fileName = "DataBaseConfigTemplate";
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
            excelService.dataBaseConfigExcel(outputStream, "数据库信息批量新增模板");
        } catch (IOException e) {
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            OutputStream ps = response.getOutputStream();
            ps.write("文件下载失败".getBytes(StandardCharsets.UTF_8));
            ps.flush();
            ps.close();
        }
    }

    @Override
    @Transactional
    public boolean upload(MultipartFile file) throws IOException {
        // 解析模板数据
        DataBaseConfigDataListener configDataListener = new DataBaseConfigDataListener();
        EasyExcel.read(file.getInputStream(), DataBaseConfigTemplatePojo.class, configDataListener)
                .sheet().doRead();
        List<DataBaseConfigTemplatePojo> dataList = configDataListener.getDataList();
        // 组装入库数据
        if (dataList.isEmpty()) {
            throw new DataException(DataException.EXCEL_FAILED, "EXCEL imported data is empty!");
        }
        List<DataBaseEntity> dataBaseEntityList = new ArrayList<>();
        for (DataBaseConfigTemplatePojo templatePojo : dataList) {
            DataBaseEntity dataBaseEntity = new DataBaseEntity();
            DBType dbType;
            if (StringUtils.isEmpty(templatePojo.getDataBaseName())) {
                dbType = DBType.getDBType(templatePojo.getUrl());
            } else {
                dbType = DBType.recognizeDbType(templatePojo.getDataBaseName());
                dataBaseEntity.setDataBaseName(templatePojo.getDataBaseName());
            }
            dataBaseEntity.setDataBaseCode(dbType.getIndex());
            dataBaseEntity.setDataBaseName(dbType.name());
            dataBaseEntity.setUrl(templatePojo.getUrl());
            // 密码加密
            String pwd = TEAUtil.encode(templatePojo.getPwd());
            dataBaseEntity.setPwd(pwd);
            dataBaseEntity.setUserName(templatePojo.getUserName());
            dataBaseEntityList.add(dataBaseEntity);
        }
        // 批量插入
        metaMapper.batchInsertDataBase(dataBaseEntityList);
        return true;
    }
}
