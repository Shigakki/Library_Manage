package com.wen.entity;

import java.util.Date;

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
public class BorrowQueue {
    private Integer id;
    private Integer user_id;
    private String user_name;//用户名
    private String phone;//手机号
    private Integer book_id;

    private Date queue_date;//进入等待队列的时间

    private Integer queue_position;//在等待队列的位置

    private String title;//书名
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getBook_id() {
        return book_id;
    }

    public void setBook_id(Integer book_id) {
        this.book_id = book_id;
    }

    public Date getQueue_date() {
        return queue_date;
    }

    public void setQueue_date(Date queue_date) {
        this.queue_date = queue_date;
    }

    public Integer getQueue_position() {
        return queue_position;
    }

    public void setQueue_position(Integer queue_position) {
        this.queue_position = queue_position;
    }
}
