package com.dollar.penguin.user.mapper;

import com.dollar.penguin.user.model.entity.UserInformationEntity;
import com.dollar.penguin.user.model.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserManageMapper {

    UserInformationEntity findUserInformationByCriteria(UserVo userVo);

    int countUserInformationByCriteria(@Param("userName") String userName,
            @Param("nickName") String nickName, @Param("emailAddress") String emailAddress);

    int insertUser(UserInformationEntity userInformation);

    UserInformationEntity findUserInfo(@Param("userId") String userId,
            @Param("userName") String userName, @Param("nickName") String nickName,
            @Param("emailAddress") String emailAddress);

}
