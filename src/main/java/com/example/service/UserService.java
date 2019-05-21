package com.example.service;

import com.example.mapper.UserMapper;
import jdk.internal.org.jline.utils.Colors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

@Service
@Transactional
public class UserService {

    @Autowired
    UserMapper users;

    public long create(Map<String, Object> user) {
        if(users.create(user) == 1)
            return users.lastSeq();
        else
            throw new RuntimeException("failed to create");
    }

    public Map<String, Object> read(long seq) {
        return users.read(seq);
    }

    public void update(Map<String, Object> user) {
        if(users.update(user) == 0)
            throw new RuntimeException("failed to update");
    }

    public void delete(long seq) {
        if(users.delete(seq) == 0)
            throw new RuntimeException("failed to delete");
    }

    public List<Map<String, Object>> list(Integer size, Integer page) {
        if(size != null && page != null)
            return users.list(size.intValue(), page.intValue());
        else
            return users.list();
    }

    public Map<String, Object> get(long seq) {
        return users.read(seq);
    }
}
