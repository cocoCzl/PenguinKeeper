package com.dollar.penguin.meta.controller;

import com.dollar.penguin.common.DataException;
import com.dollar.penguin.meta.model.entity.DataBaseEntity;
import com.dollar.penguin.meta.model.vo.DataBaseVo;
import com.dollar.penguin.meta.service.DataBaseMetaService;
import com.dollar.penguin.util.Result;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/meta/database")
public class DataBaseMetaController {

    @Autowired
    private DataBaseMetaService dataBaseMetaService;

    @RequestMapping(value = "/queryDataBase", method = RequestMethod.GET)
    public Result queryDataBase(@RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "iDisplayStart", required = true) Integer iDisplayStart,
            @RequestParam(value = "iDisplayLength", required = true) Integer iDisplayLength) {
        try {
            PageInfo<DataBaseEntity> dataList = dataBaseMetaService.queryDataBase(code, name,
                    iDisplayStart, iDisplayLength);
            return Result.success(dataList);
        } catch (Throwable e) {
            log.error("queryDataBase error:{}", e.getMessage(), e);
            if (e instanceof DataException dataBaseMetaException) {
                return Result.failure(dataBaseMetaException.getCode(),
                        dataBaseMetaException.getMessage());
            }
            return Result.failure("[查询数据库信息异常]");
        }
    }

    @RequestMapping(value = "/insertDataBase", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result insertDataBase(@RequestBody DataBaseVo dataBaseVo) {
        try {
            if (log.isInfoEnabled()) {
                log.info("insertDataBase, dataBaseVo:{}", dataBaseVo);
            }
            boolean success = dataBaseMetaService.insertDataBaseInformation(dataBaseVo);
            if (success) {
                return Result.success();
            } else {
                return Result.failure("[新增数据库信息失败]");
            }
        } catch (Exception e) {
            log.error("insertDataBase error:{}", e.getMessage(), e);
            if (e instanceof DataException dataBaseMetaException) {
                return Result.failure(dataBaseMetaException.getCode(),
                        dataBaseMetaException.getMessage());
            }
            return Result.failure("[新增数据库信息异常]");
        }
    }

    @RequestMapping(value = "/modifyDataBase", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result modifyDataBase(@RequestBody DataBaseVo dataBaseVo) {
        try {
            if (log.isInfoEnabled()) {
                log.info("modifyDataBase, dataBaseVo:{}", dataBaseVo);
            }
            boolean success = dataBaseMetaService.modifyDataBaseInformation(dataBaseVo);
            if (success) {
                return Result.success();
            } else {
                return Result.failure("[修改数据库信息失败]");
            }
        } catch (Exception e) {
            log.error("modifyDataBase error:{}", e.getMessage(), e);
            if (e instanceof DataException dataBaseMetaException) {
                return Result.failure(dataBaseMetaException.getCode(),
                        dataBaseMetaException.getMessage());
            }
            return Result.failure("[修改数据库信息异常]");
        }
    }

    @RequestMapping(value = "/deleteDataBase", method = RequestMethod.GET)
    public Result deleteDataBase(@RequestParam(value = "id", required = true) Integer id) {
        try {
            if (log.isInfoEnabled()) {
                log.info("deleteDataBase, id:{}", id);
            }
            boolean success = dataBaseMetaService.deleteDataBaseInformation(id);
            return Result.success(success);
        } catch (Throwable e) {
            log.error("deleteDataBase error:{}", e.getMessage(), e);
            if (e instanceof DataException dataBaseMetaException) {
                return Result.failure(dataBaseMetaException.getCode(),
                        dataBaseMetaException.getMessage());
            }
            return Result.failure("[删除数据库信息异常]");
        }
    }
}
