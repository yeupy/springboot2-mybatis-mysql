package com.example.mapper;

import org.jboss.logging.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    final Logger log = Logger.getLogger(this.getClass());

    @Autowired
    UserMapper users;

    private Map<String, Object> insertUser() {
        Map<String, Object> user = new HashMap<>();
        user.put("name", "name-" + Instant.now().toEpochMilli());
        user.put("tel", "010-0000-0000");
        users.create(user);
        return user;
    }

    @Test
    public void create() {
        Map<String, Object> user = new HashMap<>();
        user.put("name", "name-" + Instant.now().toEpochMilli());
        user.put("tel", "010-0000-0000");
        int res = users.create(user);
        assertEquals(1, res);

        long seq = users.lastSeq();
        Map<String, Object> userRead = users.read(seq);
        log.info(userRead);
    }

    @Test
    public void read() {
        Map<String, Object> user = this.insertUser();
        log.info(user);

        long seq = users.lastSeq();
        Map<String, Object> userRead = users.read(seq);
        log.info(userRead);

        assertEquals(user.get("name"), userRead.get("name"));
        assertEquals(user.get("tel"), userRead.get("tel"));
    }

    @Test
    public void update() throws InterruptedException {
        this.insertUser();
        long seq = users.lastSeq();
        Map<String, Object> userRead = users.read(seq);
        log.info(userRead);

        Thread.sleep(1000);
        userRead.replace("name", "new-name");
        users.update(userRead);

        Map<String, Object> userUpdated = users.read(seq);
        log.info(userUpdated);
        assertEquals("new-name", userUpdated.get("name"));

        Instant inserted = Timestamp.valueOf(userUpdated.get("insertedDatetime").toString()).toInstant();
        log.info(inserted);

        Instant updated = Timestamp.valueOf(userUpdated.get("updatedDatetime").toString()).toInstant();
        log.info(updated);

        assertNotEquals(inserted, updated);
    }

    @Test
    public void delete() {
        this.insertUser();
        long seq = users.lastSeq();

        users.delete(seq);
        Map<String, Object> userRead = users.read(seq);
        log.info(userRead);
        assertNull(userRead);
    }

    @Test
    public void list() {
        users.deleteAll();

        String firstPageUserName = this.insertUser().get("name").toString();
        this.insertUser();
        this.insertUser();
        this.insertUser();
        this.insertUser();

        String secondPageUserName = this.insertUser().get("name").toString();
        this.insertUser();
        this.insertUser();
        this.insertUser();
        this.insertUser();

        List<Map<String, Object>> page0 = users.list(5, 0);
        log.info(page0);
        assertEquals(5,page0.size());
        assertEquals(firstPageUserName, page0.get(0).get("name").toString());

        List<Map<String, Object>> page1 = users.list(5, 5);
        log.info(page1);
        assertEquals(5,page1.size());
        assertEquals(secondPageUserName, page1.get(0).get("name").toString());
    }

    @Test
    public void lastSeq() {
        this.insertUser();
        long seq = users.lastSeq();
        Map<String, Object> user = users.read(seq);
        log.info(seq);
        log.info(user);
        assertEquals(seq, user.get("seq"));
    }

}