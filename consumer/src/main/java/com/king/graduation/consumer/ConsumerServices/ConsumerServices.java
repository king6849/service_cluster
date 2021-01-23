package com.king.graduation.consumer.ConsumerServices;

import Enties.User;
import com.king.graduation.consumer.Pojo.BuyTicketPojo;
import com.king.graduation.consumer.utils.ResultVO;
import com.king.graduation.consumer.utils.ResultVOForType;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author king
 * @date 2020/11/19
 */
public interface ConsumerServices {

    /**
     * @return
     * @Describe 用户注册
     * @Author king
     * @Date 2020/11/19
     */
    ResultVO register(String phone, String password);

    ResultVO login(String loginType, User user, String code);

    ResultVO getAndSaveCode(String phone);

    /**
     * @Describe 买票
     * @Author king
     * @Date 2020/12/2
     */
    ResultVO BuyTicket(BuyTicketPojo ticket, String token);

    /**
     * @Describe 获取剩余容量
     * @Author king
     * @Date 2020/12/2
     */
    ResultVO loadTicketInfo();


    ResultVO loadShoppingCarInfo();

    /**
     * @Describe 加载个人信息
     * @Author king
     * @Date 2020/12/2
     */
    ResultVOForType loadPersonalInfo(String token);

    /**
     * @Describe 更新个人信息
     * @Author king
     * @Date 2020/11/30
     */
    ResultVO updatePersonalInfo(User user);

    /**
     * @Describe 更新头像
     * @Author king
     * @Date 2020/12/1
     */
    ResultVO upDateAvatar(MultipartFile avatar, String oldAvatar, String token);

    /**
     * @Describe 验证密码
     * @Author king
     * @Date 2020/12/1
     */
    ResultVO verifyPassword(String password, String token);
}
