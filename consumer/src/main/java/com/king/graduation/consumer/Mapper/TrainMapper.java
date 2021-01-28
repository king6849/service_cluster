package com.king.graduation.consumer.Mapper;

import Enties.Train;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author king
 * @date 2021/1/25 - 下午8:29
 */
@Mapper
public interface TrainMapper extends BaseMapper<Train> {


    @Insert("insert into train(make_time, members, c_id, i_id, u_time)  VALUES(#{makeTime},#{members},#{cId} ,#{iId},#{uTime})")
    int insertTrainRecord(Train train);

}
