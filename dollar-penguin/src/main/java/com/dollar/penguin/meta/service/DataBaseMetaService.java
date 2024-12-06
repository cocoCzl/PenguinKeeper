package com.dollar.penguin.meta.service;

import com.dollar.penguin.meta.model.entity.DataBaseEntity;
import com.dollar.penguin.meta.model.vo.DataBaseVo;
import com.github.pagehelper.PageInfo;

public interface DataBaseMetaService {

    PageInfo<DataBaseEntity> queryDataBase(String code, String name, int iDisplayStart, int iDisplayLength);

    boolean insertDataBaseInformation(DataBaseVo dataBaseVo);

    boolean modifyDataBaseInformation(DataBaseVo dataBaseVo);

    boolean deleteDataBaseInformation(int id);
}
