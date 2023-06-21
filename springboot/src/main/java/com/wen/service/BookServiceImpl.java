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
public class BookServiceImpl {
    @Autowired
    private BookMapper bookMapper;

    //查询某指定书本可借阅余量
    public Integer getBookAvailable(Integer bookId){
        return bookMapper.getBookAvailable(bookId);
    }

    //能否借书 这里规定没种书每个用户只能借一本，返回0表示不可借（已借未还，或是已经加入某书的等待队列）
    public boolean canBorrowBook(Integer userId,Integer bookId){
        String sql="select * from borrow_records where user_id="+userId+" and book_id="+bookId+"and return_date=NULL";
        List<Record> records=bookMapper.getRecords(sql);
        if(records.size()>0){
            return false;
        }
        BorrowQueue bq=bookMapper.getQueueSpecific(userId,bookId);
        if(bq!=null){
            return false;
        }
        return true;
    }

    //借书 返回-1表示不可以借，返回0表示直接借，返回其他数字表示加入某书的等待队列，返回其位置
    public Integer borrowBook(Integer userId,Integer bookId){
        if(canBorrowBook(userId,bookId)==false){
            return -1;
        }
        Integer avail=getBookAvailable(bookId);
        if(avail>0){
            bookMapper.updateBookAvailable(bookId,-1);
            Date date=new Date();
            bookMapper.addBorrowRecord(bookId,userId,date);
            return 0;
        }else{
            Date date=new Date();
            bookMapper.pushQueueBack(bookId,userId,date);
            BorrowQueue bq=bookMapper.getQueueSpecific(userId,bookId);
            return bq.getQueue_position();
        }
    }

    //还书
    public boolean returnBook(Integer userId,Integer bookId,Integer rating,String comment){
        Date return_date=new Date();
        bookMapper.returnBook(bookId,userId,return_date,rating,comment);
        Integer length=bookMapper.getQueueLength(bookId);
        if(length==0){
            bookMapper.updateBookAvailable(bookId,1);
            return true;
        }else{
            BorrowQueue bq=bookMapper.getQueueFront(bookId);
            bookMapper.popQueueFront(bookId);
            Date borrow_date=new Date();
            bookMapper.addBorrowRecord(bookId,bq.getUser_id(),borrow_date);
            return true;
        }
    }

    //取消预约(排队)
    public boolean cancelReserve(Integer bookId,Integer userId){
        return bookMapper.popQueueById(bookId,userId);
    }

    //查询借阅记录，可指定书，用户，也可什么都不指定
    public List<Record> getRecords(Integer userId,Integer bookId){
        String sql="select * from borrow_records where id is not NULL";
        if(userId!=null){
            sql+="and user_id="+userId;
        }
        if(bookId!=null){
            sql+="and book_id="+bookId;
        }
        sql+="group by book_id order by borrow_date";
        List<Record>result=bookMapper.getRecords(sql);
        return result;
    }

    //查询用户的排队信息
    public List<BorrowQueue> getQueue(Integer userId,Integer bookId){
        if(bookId==null){
            return bookMapper.getQueue(userId);
        }else{
            List<BorrowQueue> result=new LinkedList<>();
            result.add(bookMapper.getQueueSpecific(userId,bookId));
            return result;
        }
    }
}
