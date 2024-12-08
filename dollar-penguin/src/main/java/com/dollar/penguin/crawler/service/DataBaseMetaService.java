package com.dollar.penguin.crawler.service;

import com.dollar.penguin.crawler.model.entity.DataBaseEntity;
import com.dollar.penguin.crawler.model.vo.DataBaseVo;
import com.github.pagehelper.PageInfo;

public interface DataBaseMetaService {

    PageInfo<DataBaseEntity> queryDataBase(String code, String name, int iDisplayStart, int iDisplayLength);

    boolean insertDataBaseInformation(DataBaseVo dataBaseVo);

    boolean modifyDataBaseInformation(DataBaseVo dataBaseVo);

    boolean deleteDataBaseInformation(int id);
}
