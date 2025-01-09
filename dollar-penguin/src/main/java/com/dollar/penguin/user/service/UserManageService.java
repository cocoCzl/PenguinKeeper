package com.dollar.penguin.user.service;

import com.dollar.penguin.user.model.vo.UserVo;

public interface UserManageService {

    String login(UserVo userVo);

    boolean register(UserVo userVo);
}
