package com.wen.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wen.common.Result;
import com.wen.entity.Book;
import com.wen.entity.BorrowQueue;
import com.wen.entity.Record;
import com.wen.mapper.BookMapper;
import com.wen.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/book")
@Api(value = "book-controller")
public class BookController {

    @Resource
    BookMapper BookMapper;

    @Autowired
    BookService bookService;
//    添加
    @PostMapping
    @ApiOperation(value = "添加书籍")
    public Result<?> save(@RequestBody Book Book){
        BookMapper.insert(Book);
        return Result.success();
    }
//    更新
    @PutMapping
    @ApiOperation(value = "更新书籍信息")
    public Result<?> update(@RequestBody Book Book){
        BookMapper.updateById(Book);
        return Result.success();
    }
    //    s删除
    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据id删除用户")
    public Result<?> delete(@PathVariable Long id){
        BookMapper.deleteById(id);
        return Result.success();
    }
//    查询
    @GetMapping
    @ApiOperation(value = "数据库的分页查询")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<Book> wrapper = Wrappers.<Book>lambdaQuery();
        if (StrUtil.isNotBlank(search)){
            wrapper.like(Book::getTitle,search);
        }
        Page<Book> BookPage = BookMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(BookPage);
    }


    /** @Description:以下内容为陈航所写
     * @Author: 陈航
     * @Date: 2023/6/21 18:57
     */
    //借阅信息查询
    @GetMapping("/borrowRecords")
    @ApiOperation(value = "查询借阅记录，可指定书，用户，也可什么都不指定")
    public Result<?> getBorrowRecords(@RequestParam("userId") Integer userId,@RequestParam(required = false,value="bookId" ) Integer bookId,@RequestParam(required = false,value="pageSize",defaultValue = "10") Integer pageSize,@RequestParam(required = false,value="pageNum",defaultValue = "1") Integer pageNum){
        List<Record> data=bookService.getRecords(userId,bookId,pageSize,pageNum);
        return Result.success(data);
    }

    //查询特定用户的排队信息
    @GetMapping("/borrowQueue")
    @ApiOperation(value = "查询用户的排队信息，其中可指定userId,bookId")
    public Result<?> getQueue(@RequestParam(required = false,value="userId") Integer userId,@RequestParam(required = false,value="bookId") Integer bookId,@RequestParam(required = false,value="pageSize",defaultValue = "10") Integer pageSize,@RequestParam(required = false,value="pageNum",defaultValue = "1") Integer pageNum){
        List<BorrowQueue> data=bookService.getQueue(userId,bookId,pageSize,pageNum);
        return Result.success(data);
    }

    //取消预约(排队)
    @DeleteMapping("/cancelReserve")
    @ApiOperation(value = "取消用户的预约信息，其中必须指定userId和bookId")
    public Result<?> cancelReserve(@RequestParam("userId") Integer userId,@RequestParam("bookId") Integer bookId){
        boolean flag=bookService.cancelReserve(bookId,userId);
        if(flag==false){
            return Result.error("6","当前用户没有预定该书本");
        }else{
            return Result.success("取消预约成功");
        }
    }

    //借书
    @PostMapping("/borrowBook")
    @ApiOperation(value = "借书，需指定userId,bookId，其结果有三种可能:" +
            "返回-1表示不可以借" +
            "返回0表示书有余量可直接借" +
            "返回其他数字表示用户加入某书的等待队列，返回的是他在队列中的位置，" +
            "比如返回1表示该用户在队首第一个位置")
    public Result<?> borrowBook(@RequestParam("userId") Integer userId,@RequestParam("bookId") Integer bookId){
        Integer result=bookService.borrowBook(userId,bookId);
        if(result==-1){
            return Result.error("7","当前用户已借阅或已预定该书本，不可重复借阅");
        }else if(result==0){
            return Result.success("借书成功");
        }else{
            return Result.success("该书本已借完，自动为您预约，您在预约中排第"+result+"位");
        }
    }

    //还书与评论打分
    @PutMapping("/returnBook")
    @ApiOperation(value = "还书与评价，需指定userId与bookId,打分信息rating和评论信息，当用户还完书时，如果有别的用户在等待，会自动让队首获得借阅" +
            "如果没有，就让书的可借阅余量+1")
    public Result<?> returnBook(@RequestParam("userId") Integer userId,@RequestParam("bookId") Integer bookId,@RequestParam("rating")Integer rating,@RequestParam("comment")String comment){
        boolean flag=bookService.returnBook(userId,bookId,rating,comment);
        if(flag==true){
            return Result.success("还书成功");
        }else{
            return Result.error("8","还书失败，用户已还书或未借阅");
        }
    }
}
