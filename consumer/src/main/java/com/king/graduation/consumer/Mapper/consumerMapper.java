package com.king.graduation.consumer.Mapper;

import Enties.TicketRecord;
import Enties.TicketType;
import Enties.User;
import com.king.graduation.consumer.Pojo.LoginUserPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author king
 * @date 2020/11/19
 */
@Mapper
public interface consumerMapper {

    int register(@Param("phone") String phone, @Param("password") String password);

    User accountLogin(String phone);

    int weChatLogin(String openId);

    long exitOpenId(String openId);

    //获取TicketTypeId
    TicketType getTicketTypeId(String ticketName);

    //新添购票记录
    int buyATicket(TicketRecord ticket);

    //更新票数
    int desTicketNumbers(TicketRecord ticket);

    //    检查票数
    int ticketNumbers(@Param("ticketId") long ticketId,@Param("ticketName") String ticketName);

    //加载ticketName信息
    TicketType ticketTypeInfo(@Param("ticketId") long ticketId,@Param("ticketName") String ticketName);

    //拉取个人信息
    LoginUserPojo loadPersonalInfo(long id);

    //更新手机，性别，昵称，密码
    int updatePersonal(User user);

    //更新头像
    int updateAvatar(@Param("avatar") String avatar, @Param("id") long id);

    //获取密码
    String getPassword(long id);
}
