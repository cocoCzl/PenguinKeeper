package com.dollar.penguin.crawler.mapper;

import com.dollar.penguin.crawler.model.entity.DataBaseEntity;
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

}
