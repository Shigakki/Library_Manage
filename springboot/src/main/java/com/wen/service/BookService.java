package com.wen.service;

import com.wen.entity.BorrowQueue;
import com.wen.entity.Record;
import com.wen.mapper.BookMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/** @Description:
 * @Author: 陈航
 * @Date: 2023/6/21 10:35
 */
public interface BookService {


    //查询某指定书本可借阅余量
    public Integer getBookAvailable(Integer bookId);

    //能否借书 这里规定没种书每个用户只能借一本，返回0表示不可借（已借未还，或是已经加入某书的等待队列）
    public boolean canBorrowBook(Integer userId,Integer bookId);

    //借书 返回-1表示不可以借，返回0表示直接借，返回其他数字表示加入某书的等待队列，返回其位置
    public Integer borrowBook(Integer userId,Integer bookId);

    //还书
    public boolean returnBook(Integer userId,Integer bookId,Integer rating,String comment);

    //取消预约(排队)
    public boolean cancelReserve(Integer bookId,Integer userId);

    //查询借阅记录，可指定书，用户，也可什么都不指定
    public List<Record> getRecords(Integer userId,Integer bookId,Integer pageSize,Integer pageNum);

    //查询用户的排队信息
    public List<BorrowQueue> getQueue(Integer userId,Integer bookId,Integer pageSize,Integer pageNum);
}
