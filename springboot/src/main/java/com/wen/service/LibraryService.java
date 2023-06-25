package com.wen.service;

import com.wen.entity.LibStatic;
import com.wen.entity.LibraryInfo;
import org.apache.tomcat.jni.Library;

public interface LibraryService {


    /**
     * 获取图书馆的基本信息
     */
    LibraryInfo getBasicInfo();

    /**
     * 更新图书馆的基本信息
     */
    int updateBasicInfo(LibraryInfo libraryInfo);

    LibStatic getStatic();
}
