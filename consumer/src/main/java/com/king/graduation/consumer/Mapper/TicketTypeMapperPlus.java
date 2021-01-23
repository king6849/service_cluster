package com.king.graduation.consumer.Mapper;

import Enties.TicketType;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author king
 * @date 2020/12/2
 */
@Mapper
public interface TicketTypeMapperPlus extends BaseMapper<TicketType> {

    /**
     * @Describe 加载购物车基本信息
     * @Author king
     * @Date 2021/1/23 - 22:19
     * @Params []
     */
    @Select("select id ,ticket_name as ticketName,ticket_price as ticketPrice from ticket_type")
    List<TicketType> loadShoppingCarInfo();

}
