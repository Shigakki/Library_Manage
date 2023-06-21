package com.wen;

import com.wen.entity.BorrowQueue;
import com.wen.mapper.BookMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    @Resource
    private BookMapper bookMapper;
    @Test
    void contextLoads() {
        Date date = new Date();
        bookMapper.pushQueueBack(1,1,date);
        List<BorrowQueue>result=bookMapper.getQueue(1);

    }

}
