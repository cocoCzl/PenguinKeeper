package com.dollar.penguin.meta.mapper;

import com.dollar.penguin.meta.model.entity.DataBaseEntity;
import com.dollar.penguin.meta.model.entity.TableInfoEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MetaMapper {

    List<DataBaseEntity> queryDataBase(@Param("code") String code, @Param("name") String name);

    int insertDataBase(DataBaseEntity dataBaseEntity);

    int modifyDataBase(DataBaseEntity dataBaseEntity);

    int deleteDataBase(int id);

    DataBaseEntity queryDataBaseById(int id);

    void batchInsertTable(List<TableInfoEntity> tableInfoEntityList);

    int deleteTableByDataBase(int dataBaseId);

    int deleteColumnByDataBase(int dataBaseId);
}
