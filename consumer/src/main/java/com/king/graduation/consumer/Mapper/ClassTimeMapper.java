package com.king.graduation.consumer.Mapper;

import Enties.ClassTime;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author king
 * @date 2021/1/26 - 下午7:42
 */
@Mapper
public interface ClassTimeMapper extends BaseMapper<ClassTime> {

    @Select("select * from class_time")
    List<ClassTime> allClassTimeInfo();

}
