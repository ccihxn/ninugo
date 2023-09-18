package com.ks.ninugo.mapper;

import com.ks.ninugo.dto.UpdateUserDTO;
import com.ks.ninugo.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    void insertUser(UserDTO userDTO);
    UserDTO findUserByLoginId(String LoginId);
    void deleteUserById(int userId);
    void updateUser(UpdateUserDTO updateUserDTO);
    void insertQueue(int userId);
}