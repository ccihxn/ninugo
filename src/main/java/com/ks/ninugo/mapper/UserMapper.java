package com.ks.ninugo.mapper;

import com.ks.ninugo.dto.UserDTO;
//import org.apache.mybatis.annotations.Mapper;
public interface UserMapper {
    public void insertUser(UserDTO userDTO);
}
