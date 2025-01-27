package com.dollar.penguin.crawler.service.impl;

import com.dollar.penguin.common.exception.DataException;
import com.dollar.penguin.crawler.mapper.MetaMapper;
import com.dollar.penguin.crawler.model.entity.DataBaseEntity;
import com.dollar.penguin.crawler.model.vo.DataBaseVo;
import com.dollar.penguin.crawler.service.DataBaseMetaService;
import com.dollar.penguin.util.CipherUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.penguin.database.util.DBType;
import java.sql.Timestamp;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        DataBaseEntity dataBaseEntity = metaMapper.queryDataBaseByName(dataBaseVo.getDataBaseName());
        if (dataBaseEntity != null) {
            throw new DataException(DataException.DATA_INSERT_FAILED, "DataBaseByName already exists!");
        }
        // 插入数据
        dataBaseEntity = buildDataBaseEntity(dataBaseVo);
        int rsCount = metaMapper.insertDataBase(dataBaseEntity);
        if (rsCount != 1) {
            throw new DataException(DataException.DATA_INSERT_FAILED, "DataBase insert exception!");
        }
        return true;
    }

    @Override
    public boolean modifyDataBaseInformation(DataBaseVo dataBaseVo) {
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
    @Transactional
    public boolean deleteDataBaseInformation(int id) {
        // 删除数据库信息表
        metaMapper.deleteDataBase(id);
        return true;
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
        String pwd = CipherUtil.encode(dataBaseVo.getPwd());
        dataBaseEntity.setPwd(pwd);
        dataBaseEntity.setUserName(dataBaseVo.getUserName());
        return dataBaseEntity;
    }
}
