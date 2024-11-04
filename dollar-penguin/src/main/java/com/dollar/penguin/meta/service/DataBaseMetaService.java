package com.dollar.penguin.meta.service;

import com.dollar.penguin.meta.model.DataBaseEntity;
import com.dollar.penguin.meta.model.vo.DataBaseVo;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface DataBaseMetaService {

    PageInfo<DataBaseEntity> queryDataBase(String code, String name, int iDisplayStart, int iDisplayLength);

    boolean insertDataBaseInformation(DataBaseVo dataBaseVo);

    void download(HttpServletResponse response) throws IOException;

    boolean upload(MultipartFile file) throws IOException;
}
