package com.king.graduation.consumer.ConsumerServices;

import com.king.graduation.consumer.utils.ResultVO;

/**
 * 游泳馆主要业务
 *
 * @Author king
 * @date 2020/11/19
 */
public interface CoreService {


    /**
     * @Describe 报名培训
     * @Author king
     * @Date 2021/1/25 - 下午8:52
     * @Params [token, classId]
     */
    ResultVO scheduleTraining(String token, long classId,long uTime);

    /**
     * @Describe 加载开设的班级信息
     * @Author king
     * @Date 2021/1/25 - 下午9:24
     * @Params []
     */
    ResultVO trainClassInfoList();


}
