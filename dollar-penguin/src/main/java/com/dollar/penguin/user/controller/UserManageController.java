package com.dollar.penguin.user.controller;

import com.dollar.penguin.user.model.vo.UserVo;
import com.dollar.penguin.user.service.UserManageService;
import com.dollar.penguin.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/manage/user")
public class UserManageController {

    @Autowired
    private UserManageService userManageService;

    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result<String> login(@RequestBody UserVo userVo) {
        if (log.isInfoEnabled()) {
            log.info("Login:{}", userVo);
        }
        String token = userManageService.login(userVo);
        return Result.success(token);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result<String> register(@RequestBody UserVo userVo) {
        if (log.isInfoEnabled()) {
            log.info("Register:{}", userVo);
        }
        boolean success = userManageService.register(userVo);
        if (success) {
            return Result.success("[注册成功]");
        } else {
            return Result.failure("[注册失败]");
        }
    }
}
