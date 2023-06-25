package com.wen.mapper;

import com.wen.entity.LibraryInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryMapper {
    @Select("SELECT * FROM library_info;")
    public List<LibraryInfo> getBasicInfo();
    @Update("Update library_info set libraryName=#{libraryInfo.libraryName},openTime=#{libraryInfo.openTime}," +
            "closeTime=#{libraryInfo.closeTime},description=#{libraryInfo.description} where id=1;")
    int updateBasicInfo(@Param(value="libraryInfo") LibraryInfo libraryInfo);

    @Select("SELECT COUNT(*) FROM book")
    int getTotalNum();

    @Select("SELECT COUNT(*) FROM borrow_records")
    int getBorrowedNum();

    @Select("SELECT COUNT(*) FROM user")
    int getUserNum();

    @Select("SELECT COUNT(*) FROM borrow_queue")
    int getReservedNum();

}
