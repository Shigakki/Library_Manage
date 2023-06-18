package com.wen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("book")//mybatis-plus
@Data
public class Book {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;

    private String author;

    private String description;

    private Integer totalCopies;

    private Integer availableCopies;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;
}
