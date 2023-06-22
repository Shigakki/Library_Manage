package com.wen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer user_id;

    private Integer book_id;

    private Date borrow_date;

    private Date return_date;

    private String comment;
    private Integer rating;
    private String title;//书名
    private String user_name;//用户名
    private String phone;//手机号

}
