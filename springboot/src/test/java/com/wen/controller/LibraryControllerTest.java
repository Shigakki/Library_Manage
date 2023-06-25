package com.wen.controller;


import com.alibaba.fastjson.JSON;
import com.wen.entity.Book;
import com.wen.entity.LibraryInfo;
import com.wen.mapper.BookMapper;
import com.wen.mapper.LibraryMapper;
import com.wen.service.BookService;
import com.wen.service.LibraryService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.logging.LogManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class LibraryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LibraryService libraryService;

    @InjectMocks
    private LibraryController libraryController;

    @Mock
    private LibraryMapper libraryMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(libraryController).build();
    }


    @Test
    void getBasicInfo() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/library/basicInfo")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void updateBasicInfo() {
        LibraryInfo libraryInfo=new LibraryInfo();
        libraryInfo.setLibraryName("思源楼图书");
        libraryInfo.setDescription("思源楼是坠吼的！");
        libraryInfo.setOpenTime("7:05");
        libraryInfo.setCloseTime("23:55");
        // Set book properties

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/library/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content((JSON.toJSONString(libraryInfo))))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.code").value(0))
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    void getStatic() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/library/getStatic")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.code").value(0))
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}