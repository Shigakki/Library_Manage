package com.wen;

import com.wen.entity.BorrowQueue;
import com.wen.entity.Record;
import com.wen.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
@SpringBootTest
class DemoApplicationTests {
    @Autowired
    BookService bookService;
    @Test
    void totalTest() {

        //初始book1有3本可借
        int avail=bookService.getBookAvailable(1);
        System.out.println(avail);

        //应返回0直接借
        Integer borrow_result=bookService.borrowBook(1,1);
        System.out.println(borrow_result);

        //应返回-1不可借
        borrow_result=bookService.borrowBook(1,1);
        System.out.println(borrow_result);

        //2,3可借 4,5,6到队列中
        for(int i=2;i<=6;i++){
            borrow_result=bookService.borrowBook(i,1);
            System.out.println(borrow_result);
        }

        //应返回-1
        borrow_result=bookService.borrowBook(6,1);
        System.out.println(borrow_result);

        //6取消预约
        boolean result=bookService.cancelReserve(1,4);
        System.out.println(result);

        //1号还书,此时5号应自动借到书
        result=bookService.returnBook(1,1,5,"123");
        System.out.println(result);

        List<Record>records=bookService.getRecords(null,1,10,1);
        for(int i=0;i<records.size();i++){
            System.out.println(records.get(i));
        }

        List<BorrowQueue>queues=bookService.getQueue(null,null,10,1);
        for(int i=0;i<queues.size();i++){
            System.out.println(queues.get(i));
        }
    }


}
