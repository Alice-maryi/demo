package com.demo.auth.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("photo_data")
public class PhotoData {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField("name")
    private String name;
    @TableField("sort")
    private Integer sort;
    @TableField("file_type")
    private Integer fileType;
    @TableField("parent_name")
    private String parentName;
    @TableField("data_content")
    private String dataContent;

}