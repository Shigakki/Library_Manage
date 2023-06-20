package com.wen.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wen.common.Result;
import com.wen.entity.Book;
import com.wen.mapper.BookMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/book")
@Api(value = "book-controller")
public class BookController {
    @Autowired
    @Resource
    BookMapper BookMapper;

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
}
