package com.dollar.penguin.crawler.controller;

import com.dollar.penguin.crawler.model.entity.DataBaseEntity;
import com.dollar.penguin.crawler.model.vo.DataBaseVo;
import com.dollar.penguin.crawler.service.DataBaseMetaService;
import com.dollar.penguin.util.Result;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/meta/database")
public class DataBaseMetaController {

    @Autowired
    private DataBaseMetaService dataBaseMetaService;

    @GetMapping(value = "/queryDataBase")
    public Result<PageInfo<DataBaseEntity>> queryDataBase(@RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "iDisplayStart", required = true) Integer iDisplayStart,
            @RequestParam(value = "iDisplayLength", required = true) Integer iDisplayLength) {
        if (log.isInfoEnabled()) {
            log.info("queryDataBase, code:{},name:{}", code, name);
        }
        PageInfo<DataBaseEntity> dataList = dataBaseMetaService.queryDataBase(code, name,
                iDisplayStart, iDisplayLength);
        return Result.success(dataList);
    }

    @PostMapping(value = "/insertDataBase", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result<String> insertDataBase(@RequestBody DataBaseVo dataBaseVo) {
        if (log.isInfoEnabled()) {
            log.info("insertDataBase, dataBaseVo:{}", dataBaseVo);
        }
        boolean success = dataBaseMetaService.insertDataBaseInformation(dataBaseVo);
        if (success) {
            return Result.success("[新增数据库信息成功]");
        } else {
            return Result.failure("[新增数据库信息失败]");
        }
    }

    @PutMapping(value = "/modifyDataBase", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result<String> modifyDataBase(@RequestBody DataBaseVo dataBaseVo) {
        if (log.isInfoEnabled()) {
            log.info("modifyDataBase, dataBaseVo:{}", dataBaseVo);
        }
        boolean success = dataBaseMetaService.modifyDataBaseInformation(dataBaseVo);
        if (success) {
            return Result.success("[修改数据库信息成功]");
        } else {
            return Result.failure("[修改数据库信息失败]");
        }
    }

    @GetMapping(value = "/deleteDataBase")
    public Result<String> deleteDataBase(@RequestParam(value = "id", required = true) Integer id) {
        if (log.isInfoEnabled()) {
            log.info("deleteDataBase, id:{}", id);
        }
        boolean success = dataBaseMetaService.deleteDataBaseInformation(id);
        if (success) {
            return Result.success("[删除数据库信息成功]");
        } else {
            return Result.failure("[删除数据库信息失败]");
        }
    }
}
