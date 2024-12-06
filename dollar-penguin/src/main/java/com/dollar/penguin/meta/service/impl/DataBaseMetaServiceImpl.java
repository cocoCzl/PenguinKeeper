package com.dollar.penguin.meta.service.impl;

import com.dollar.penguin.common.DataException;
import com.dollar.penguin.meta.mapper.MetaMapper;
import com.dollar.penguin.meta.model.entity.DataBaseEntity;
import com.dollar.penguin.meta.model.vo.DataBaseVo;
import com.dollar.penguin.meta.service.DataBaseMetaService;
import com.dollar.penguin.util.TEAUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.penguin.database.util.DBType;
import java.sql.Timestamp;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DataBaseMetaServiceImpl implements DataBaseMetaService {

    @Autowired
    private MetaMapper metaMapper;

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

    @Override
    public boolean modifyDataBaseInformation(DataBaseVo dataBaseVo) {
        // 数据验证
        checkData(dataBaseVo);
        // 插入数据
        DataBaseEntity dataBaseEntity = buildDataBaseEntity(dataBaseVo);
        dataBaseEntity.setId(dataBaseVo.getId());
        dataBaseEntity.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        int rsCount = metaMapper.modifyDataBase(dataBaseEntity);
        if (rsCount != 1) {
            throw new DataException(DataException.DATA_UPDATE_FAILED, "DataBase update exception!");
        }
        return true;
    }

    @Override
    public boolean deleteDataBaseInformation(int id) {
        int rsCount = metaMapper.deleteDataBase(id);
        if (rsCount != 1) {
            throw new DataException(DataException.DATA_DELETE_FAILED, "DataBase delete exception!");
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
}
