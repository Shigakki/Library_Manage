package com.wen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wen.entity.Book;
import com.wen.entity.BorrowQueue;
import com.wen.entity.Record;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/** @Description:以下部分为陈航所写
 * @Author: 陈航
 * @Date: 2023/6/20 16:30
 */
@Repository
public interface BookMapper extends BaseMapper<Book> {

    //查询可借书本余量
    public int getBookAvailable(@Param("bookId")int bookId);

    //修改可借阅书本数
    public boolean updateBookAvailable(@Param("bookId")int bookId,@Param("num")int num);

    //移出某本书等待队列的队首
    public boolean popQueueFront(@Param("bookId")int bookId);

    //得到某书队列中的队首元素
    public BorrowQueue getQueueFront(@Param("bookId")int bookId);

    //根据用户id移出某本书等待队列
    public boolean popQueueById(@Param("bookId")int bookId,@Param("userId")int userId);

    //添加到某本书等待队列的队尾
    public boolean pushQueueBack(@Param("bookId")int bookId,@Param("userId") int userId,@Param("date") Date date);

    //增加借阅信息
    public boolean addBorrowRecord(@Param("bookId")int bookId,@Param("userId")int user_Id,@Param("date") Date date);

    //查询借阅信息
    public List<Record> getRecords(@Param("sql")String sql);

    //还书反馈，填写还书时间，评分和评论
    public boolean returnBook(@Param("bookId")int bookId,@Param("userId")int userId,@Param("return_date")Date return_date,@Param("rating")int rating,@Param("comment") String comment);

    //查询用户所在的排队队列
    public List<BorrowQueue> getQueue(@Param("userId")int userId);
    //查询某个用户在某指定书本的排队信息
    public BorrowQueue getQueueSpecific(@Param("userId")int userId,@Param("bookId")int bookId);
    //得到某书排队队列长度
    public int getQueueLength(@Param("bookId")int bookId);
}

