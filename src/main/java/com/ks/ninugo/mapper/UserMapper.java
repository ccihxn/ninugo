package com.ks.ninugo.mapper;

import com.ks.ninugo.dto.UpdateUserDTO;
import com.ks.ninugo.dto.UserDTO;
//import org.apache.mybatis.annotations.Mapper;
public interface UserMapper {
    public void insertUser(UserDTO userDTO);
    public UserDTO findUserById(int userId);
    public void deleteUserById(int userId);
    public void updateUser(UpdateUserDTO updateUserDTO);
}
