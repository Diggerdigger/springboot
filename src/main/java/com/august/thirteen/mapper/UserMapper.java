package com.august.thirteen.mapper;

import com.august.thirteen.model.UserModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("insert into user (id,login,token,node_id) values (#{id},#{login},#{token},#{node_id})")
    void insert(UserModel user);

    @Select("select * from user where token=#{token}")
    UserModel findUserByToken(@Param("token") String token);
}
