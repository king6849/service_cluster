package com.king.graduation.consumer.Mapper;

import Enties.ClassInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.king.graduation.consumer.Pojo.ClassKinds;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author king
 * @date 2021/1/25 - 下午8:40
 */
@Mapper
public interface ClassInfoMapper extends BaseMapper<ClassInfo> {

    /**
     * @Describe 查看每种课程开了几个班级
     * @Author king
     * @Date 2021/1/26 - 下午8:59
     * @Params []
     */
    List<ClassKinds> classRemainder();


    List<Long> classInfoList();

    /**
     * @Describe 检查可报名人数
     * @Author king
     * @Date 2021/1/27 - 下午7:34
     * @Params [iId, uTime]
     */
    List<ClassInfo> checkRemainder(@Param("cId") long cId, @Param("uTime") long uTime);

    /**
     * @Describe 更新可报名人数
     * @Author king
     * @Date 2021/1/27 - 下午7:54
     * @Params [uTime, id]
     */
    int updateRemainder(@Param("uTime") long uTime, @Param("id") long id);
}
