package com.dollar.penguin.crawler.mapper;

import com.dollar.penguin.crawler.model.entity.DataBaseEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MetaMapper {

    List<DataBaseEntity> queryDataBase(@Param("code") String code, @Param("name") String name);

    @Select("SELECT id, databasecode, databasename, url, pwd, username, created_at, updated_at " +
            "FROM develop.database_information WHERE databasename = #{databasename}")
    DataBaseEntity queryDataBaseByName(@Param("databasename") String databasename);

    int insertDataBase(DataBaseEntity dataBaseEntity);

    int modifyDataBase(DataBaseEntity dataBaseEntity);

    @Select("delete from database_information where id = #{id}")
    int deleteDataBase(@Param("id") int id);

    @Select("select id, dataBaseCode, dataBaseName, url, pwd, username, created_at as createAt, "
            + "updated_at as updatedAt from database_information where id = #{id}")
    DataBaseEntity queryDataBaseById(@Param("id") int id);

}
