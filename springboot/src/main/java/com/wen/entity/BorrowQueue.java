package com.wen.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


public class BorrowQueue {
    private Integer id;
    private Integer user_id;

    private Integer book_id;

    private Date queue_date;

    private Integer queue_position;
}
