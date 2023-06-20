package com.wen.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("borrow_records")//mybatis-plus
@Data
public class Record {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer user_id;

    private Integer book_id;

    private Date borrow_date;

    private Date return_date;

    private String comment;

}
