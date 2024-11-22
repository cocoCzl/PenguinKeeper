package com.dollar.penguin.meta.controller;

import com.dollar.penguin.common.DataException;
import com.dollar.penguin.meta.model.entity.DataBaseEntity;
import com.dollar.penguin.meta.model.vo.DataBaseVo;
import com.dollar.penguin.meta.service.DataBaseMetaService;
import com.dollar.penguin.util.Result;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 数据库信息批量新增模板文件下载
     */
    @GetMapping("/downloadDataBases")
    public void downloadDataBases(HttpServletResponse response) throws IOException {
        dataBaseMetaService.download(response);
    }

    /**
     * 数据库信息批量新增文件上传
     */
    @PostMapping("/uploadDataBases")
    @ResponseBody
    public Result uploadDataBases(MultipartFile file) {
        try {
            boolean success = dataBaseMetaService.upload(file);
            if (success) {
                return Result.success();
            } else {
                return Result.failure("[批量新增失败]");
            }
        } catch (Exception e) {
            log.error("upload exception:<{}>", e.getMessage(), e);
            return Result.failure("[批量新增失败]");
        }
    }
}
