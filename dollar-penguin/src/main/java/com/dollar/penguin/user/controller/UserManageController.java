package com.dollar.penguin.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Validated
@RequestMapping(value = "/manage/user")
public class UserManageController {

    // TODO 用户注册需要参数校验，使用Validated @Pattern()
}
