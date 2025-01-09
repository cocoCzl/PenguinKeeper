package com.dollar.penguin.user.service.impl;

import com.dollar.penguin.common.enumUtil.LoginTypeEnum;
import com.dollar.penguin.common.exception.DataException;
import com.dollar.penguin.common.exception.WebException;
import com.dollar.penguin.user.mapper.UserManageMapper;
import com.dollar.penguin.user.model.entity.UserInformationEntity;
import com.dollar.penguin.user.model.vo.UserVo;
import com.dollar.penguin.user.service.UserManageService;
import com.dollar.penguin.util.CipherUtil;
import com.dollar.penguin.util.JwtUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserManageServiceImpl implements UserManageService {

    @Autowired
    private UserManageMapper userManageMapper;

    @Override
    public String login(UserVo userVo) {
        LoginTypeEnum loginType = LoginTypeEnum.parse(userVo.getLoginType());
        switch (loginType) {
            // 用户名登录
            case USER_NAME -> {
                return checkAndBuildToken(userVo.getUserName(), loginType, userVo);
            }
            // 昵称登录
            case NICK_NAME -> {
                return checkAndBuildToken(userVo.getNickName(), loginType, userVo);
            }
            // 用户ID登录
            case USER_ID -> {
                return checkAndBuildToken(userVo.getUserId(), loginType, userVo);
            }
            // 邮箱登录
            case EMAIL_ADDRESS -> {
                return checkAndBuildToken(userVo.getEmailAddress(), loginType, userVo);
            }
            // 登录方式异常
            default -> {
                log.error("login type error. loginType:{}", loginType);
                throw new WebException(WebException.PARAM_FAILED, "login type error.");
            }
        }
    }

    @Override
    public boolean register(UserVo userVo) {
        // 数据校验
        checkRegister(userVo);
        // 入库
        UserInformationEntity entity = buildUserInformationEntity(userVo);
        int rsCount = userManageMapper.insertUser(entity);
        if (rsCount != 1) {
            log.error("insert user error. update count:{}", rsCount);
            throw new DataException(DataException.DATA_INSERT_FAILED, "User insert exception!");
        }
        return true;
    }

    private void checkRegister(UserVo userVo) {
        // 校验用户信息
        if (StringUtils.isBlank(userVo.getUserName()) || userVo.getUserName().length() > 16) {
            throw new WebException(WebException.PARAM_FAILED,
                    "Username cannot be empty and must not exceed 16 characters.");
        }

        if (StringUtils.isBlank(userVo.getNickName()) || userVo.getNickName().length() > 16) {
            throw new WebException(WebException.PARAM_FAILED,
                    "Nickname cannot be empty and must not exceed 16 characters.");
        }

        if (StringUtils.isBlank(userVo.getPwd()) || userVo.getPwd().length() < 6
                || userVo.getPwd().length() > 32) {
            throw new WebException(WebException.PARAM_FAILED,
                    "Password cannot be empty and must be between 6 and 32 characters in length.");
        }

        if (StringUtils.isBlank(userVo.getEmailAddress())
                || userVo.getEmailAddress().length() > 32) {
            throw new WebException(WebException.PARAM_FAILED,
                    "Email address cannot be empty and must not exceed 32 characters.");
        }

        // 简单的邮箱格式校验正则表达式
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!userVo.getEmailAddress().matches(emailRegex)) {
            throw new WebException(WebException.PARAM_FAILED, "Invalid email format.");
        }

        // 3. 校验用户是否已经存在
        boolean userExists = userManageMapper.countUserInformationByCriteria(userVo.getUserName(),
                userVo.getNickName(), userVo.getEmailAddress()) > 0;
        if (userExists) {
            log.error("Username, Nickname, or Email address has been registered.");
            throw new DataException(DataException.DATA_INSERT_FAILED,
                    "Username, Nickname, or Email address has been registered.");
        }
    }

    private UserInformationEntity buildUserInformationEntity(UserVo userVo) {
        UserInformationEntity entity = new UserInformationEntity();
        String userId = UUID.randomUUID().toString();
        entity.setUserId(userId);
        entity.setUserName(userVo.getUserName());
        entity.setNickName(userVo.getNickName());
        String pwd = CipherUtil.encode(userVo.getPwd());
        entity.setPwd(pwd);
        entity.setEmailAddress(userVo.getEmailAddress());
        return entity;
    }

    private String checkAndBuildToken(String param, LoginTypeEnum loginType, UserVo userVo) {
        if (StringUtils.isBlank(param)) {
            log.error("param is empty. loginType:{},param:{}", loginType, param);
            throw new WebException(WebException.PARAM_FAILED, "login param is empty.");
        }
        UserInformationEntity userInformation = userManageMapper.findUserInformationByCriteria(
                userVo);
        if (userInformation == null) {
            log.error("The account does not exist. param:{}", userVo);
            throw new DataException(DataException.DATA_SELECT_FAILED,
                    "The account does not exist.");
        }
        String pwd = CipherUtil.decode(userInformation.getPwd());
        if (!pwd.equals(userVo.getPwd())) {
            log.error("Wrong password. param:{}", userVo);
            throw new WebException(WebException.PARAM_FAILED, "Wrong password.");
        }

        // 生成Token
        Map<String, Object> data = new HashMap<>();
        data.put("login", param);
        data.put("loginType", loginType.code());
        return JwtUtil.doGen(JwtUtil.KEY, data);
    }
}
