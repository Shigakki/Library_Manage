package com.wen.service.impl;

import com.wen.entity.LibStatic;
import com.wen.entity.LibraryInfo;
import com.wen.mapper.LibraryMapper;
import com.wen.service.LibraryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LibraryServiceImpl implements LibraryService {

    @Autowired
    private LibraryMapper libraryMapper;

    @Override
    public LibraryInfo getBasicInfo() {
        List<LibraryInfo> info =libraryMapper.getBasicInfo();
        LibraryInfo libraryInfo= info.get(0);
        System.out.println(info);
        return libraryInfo;
    }

    @Override
    public int updateBasicInfo(LibraryInfo libraryInfo) {
        int result = libraryMapper.updateBasicInfo(libraryInfo);
        return result;
    }

    @Override
    public LibStatic getStatic() {
        LibStatic libStatic=new LibStatic();
        libStatic.setTotalNum(libraryMapper.getTotalNum());
        libStatic.setBorrowedNum(libraryMapper.getBorrowedNum());
        libStatic.setUserNum(libraryMapper.getUserNum());
        libStatic.setReservedNum(libraryMapper.getReservedNum());
        return libStatic;
    }
}
