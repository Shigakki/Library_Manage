package com.wen.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("library_info")
@Data
public class LibraryInfo {
    private int id;
    private String libraryName;
    private String openTime;
    private String closeTime;
    private String description;
}
