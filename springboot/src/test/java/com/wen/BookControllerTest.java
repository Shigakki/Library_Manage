package com.wen;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wen.controller.BookController;
import com.wen.entity.Book;
import com.wen.entity.BorrowQueue;
import com.wen.entity.Record;
import com.wen.mapper.BookMapper;
import com.wen.service.BookService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.GreaterThan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookMapper bookMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void testSaveBook() throws Exception {
        Book book = new Book();
        book.setAuthor("zjx");
        book.setTitle("api design");
        // Set book properties
        when(bookMapper.insert(any(Book.class))).thenReturn(0);

        mockMvc.perform(MockMvcRequestBuilders.post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((JSON.toJSONString(book))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    @Test
    public void testUpdateBook() throws Exception {
        Book book = new Book();
        // Set book properties

        when(bookMapper.updateById(any(Book.class))).thenReturn(0);

        mockMvc.perform(MockMvcRequestBuilders.put("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    @Test
    public void testDeleteBook() throws Exception {
        Long bookId = 1L;

        when(bookMapper.deleteById(anyLong())).thenReturn(0);

        mockMvc.perform(MockMvcRequestBuilders.delete("/book/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    @Test
    public void testFindPage() throws Exception {

        Integer pageNum = 1;
        Integer pageSize = 10;
        String search = "";
        LambdaQueryWrapper<Book> wrapper = Wrappers.<Book>lambdaQuery();
        if (StrUtil.isNotBlank(search)){
            wrapper.like(Book::getTitle,search);
        }

        List<Book> books = new ArrayList<>(); // Mock list of books
        books.add(new Book());
        books.add(new Book());

        Page<Book> bookPage = new Page<>(pageNum, pageSize, books.size());
        bookPage.setRecords(books);

        when(bookMapper.selectPage(new Page<>(pageNum, pageSize), wrapper)).thenReturn(bookPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/book")
                        .param("pageNum", pageNum.toString())
                        .param("pageSize", pageSize.toString())
                        .param("search", search))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value("成功"));
    }

    @Test
    public void testGetBorrowRecords() throws Exception {
        Integer userId = 1;
        Integer bookId = 1;
        Integer pageSize = 10;
        Integer pageNum = 1;

        List<Record> records = Arrays.asList(new Record(), new Record()); // Mock list of records

        when(bookService.getRecords(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders.get("/book/borrowRecords")
                        .param("userId", userId.toString())
                        .param("bookId", bookId.toString())
                        .param("pageSize", pageSize.toString())
                        .param("pageNum", pageNum.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testGetQueue() throws Exception {
        Integer userId = 1;
        Integer bookId = 1;
        Integer pageSize = 10;
        Integer pageNum = 1;

        List<BorrowQueue> queues = Arrays.asList(new BorrowQueue(), new BorrowQueue()); // Mock list of queues

        when(bookService.getQueue(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(queues);

        mockMvc.perform(MockMvcRequestBuilders.get("/book/borrowQueue")
                        .param("userId", userId.toString())
                        .param("bookId", bookId.toString())
                        .param("pageSize", pageSize.toString())
                        .param("pageNum", pageNum.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testCancelReserve() throws Exception {
        Integer userId = 1;
        Integer bookId = 1;

        when(bookService.cancelReserve(anyInt(), anyInt())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/book/cancelReserve")
                        .param("userId", userId.toString())
                        .param("bookId", bookId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value("取消预约成功"));

        bookId = 222;

        when(bookService.cancelReserve(anyInt(), anyInt())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/book/cancelReserve")
                        .param("userId", userId.toString())
                        .param("bookId", bookId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.msg").value("当前用户没有预定该书本"));


    }

    @Test
    public void testBorrowBook() throws Exception {
        Integer userId = 1;
        Integer bookId = 1;

        when(bookService.borrowBook(anyInt(), anyInt())).thenReturn(0);

        mockMvc.perform(MockMvcRequestBuilders.post("/book/borrowBook")
                        .param("userId", userId.toString())
                        .param("bookId", bookId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value("借书成功"));


        bookId = 2;

        when(bookService.borrowBook(anyInt(), anyInt())).thenReturn(0);

        mockMvc.perform(MockMvcRequestBuilders.post("/book/borrowBook")
                        .param("userId", userId.toString())
                        .param("bookId", bookId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value("该书本已借完，自动为您预约，您在预约中排第3位"));

    }

    @Test
    public void testReturnBook() throws Exception {
        Integer userId = 1;
        Integer bookId = 1;
        Integer rating = 5;
        String comment = "Good book";

        when(bookService.returnBook(anyInt(), anyInt(), anyInt(), anyString())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.put("/book/returnBook")
                        .param("userId", userId.toString())
                        .param("bookId", bookId.toString())
                        .param("rating", rating.toString())
                        .param("comment", comment))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value("还书成功"));


         bookId = 222;

        when(bookService.returnBook(anyInt(), anyInt(), anyInt(), anyString())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.put("/book/returnBook")
                        .param("userId", userId.toString())
                        .param("bookId", bookId.toString())
                        .param("rating", rating.toString())
                        .param("comment", comment))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.msg").value("还书失败，用户已还书或未借阅"));
    }
}
