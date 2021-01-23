package com.king.graduation.consumer.Mapper;

import Enties.Pool;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author king
 * @date 2021/1/23 - 21:22
 */
@Mapper
public interface poolMapper extends BaseMapper<Pool> {
    /**
     * @Describe 更新票数
     * @Author king
     * @Date 2021/1/23 - 17:36
     * @Params [num, type]
     */
    @Update("update pool set total_ticket=total_ticket-${num} where id=#{id}")
    int decreaseTicketNumbers(@Param("num") int num, @Param("id") long id);

    @Select("select id,pool_name as poolName,total_ticket as totalTicket from pool")
    List<Pool> poolInfoList();

}
