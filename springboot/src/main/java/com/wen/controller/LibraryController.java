package com.wen.controller;


import com.wen.common.Result;
import com.wen.entity.LibStatic;
import com.wen.entity.LibraryInfo;
import com.wen.entity.User;
import com.wen.service.LibraryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.jni.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/library")
@Api(value = "library-controller")
public class LibraryController {

    /**@Author: YuHaonan
     * @Description: 以下为图书馆基本信息查询接口
     * @Date: 2023/6/22 2:57
     */
    @Autowired
    LibraryService libraryService;

    /**
     * 获取图书馆基本信息接口
     * @return
     */
    @GetMapping("/basicInfo")
    @ApiOperation(value = "获取图书馆基本信息")
    public Result<?> getBasicInfo(){
        LibraryInfo libraryInfo=libraryService.getBasicInfo();
        return Result.success(libraryInfo);
    }

    /**
     * 更新图书馆基本信息
     * @param libraryInfo
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新图书馆基本信息")
    public Result<?> updateBasicInfo(@RequestBody LibraryInfo libraryInfo){
        int result=libraryService.updateBasicInfo(libraryInfo);
        if(result!=0){
             return Result.success(result);
        }else{return Result.error("-1","插入失败，请检查参数！");
        }
    }

    @GetMapping("/getStatic")
    @ApiOperation(value = "获取图书馆馆藏统计信息")
    public LibStatic getStatic(){
        LibStatic libStatic=libraryService.getStatic();
        return   libStatic;
    };
}
