package com.example.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    int create(Map<String, Object> user);

    Map<String, Object> read(long seq);

    int update(Map<String, Object> user);

    int delete(long seq);

    List<Map<String, Object>> list(@Param("size")int size, @Param("page")int page);

    @Select("SELECT LAST_INSERT_ID()")
    long lastSeq();

    @Delete("DELETE FROM user")
    long deleteAll();

}
