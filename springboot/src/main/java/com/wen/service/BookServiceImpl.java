package com.wen.service;

import com.wen.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

public class BookServiceImpl {
    @Autowired
    private BookMapper bookMapper;

}
