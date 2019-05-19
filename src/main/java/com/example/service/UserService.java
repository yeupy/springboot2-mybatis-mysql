package com.example.service;

import com.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserService {

    @Autowired
    UserMapper users;

    public long insert(Map<String, Object> user) {
        int res = users.create(user);
        if(res == 1)
            return users.lastSeq();
        else
            return 0;
    }

    public List<Map<String, Object>> list() {
//        return users.list();
        return null;
    }

    public Map<String, Object> get(long seq) {
        return users.read(seq);
    }
}
