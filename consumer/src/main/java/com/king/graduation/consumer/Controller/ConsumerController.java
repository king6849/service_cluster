package com.king.graduation.consumer.Controller;

import com.king.graduation.consumer.ConsumerServices.ConsumerServices;
import com.king.graduation.consumer.Entity.User;
import com.king.graduation.consumer.Pojo.LoginUserPojo;
import com.king.graduation.consumer.Pojo.TicketRecodedPojo;
import com.king.graduation.consumer.utils.DateUtil;
import com.king.graduation.consumer.utils.ResultVO;
import com.king.graduation.consumer.utils.ResultVOForType;
import com.king.graduation.consumer.utils.TokenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Calendar;

@RestController
@RequestMapping(path = "/client")
public class ConsumerController {

    @Autowired
    private ConsumerServices consumerServicesStanderImpl;

    /**
     * @Describe 用户注册
     * @Author king
     * @Date 2020/11/20
     */
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResultVO register(@RequestBody User user) {
        return consumerServicesStanderImpl.register(user.getPhone(), user.getPassword());
    }

    /**
     * @Describe 用户登录（微信，账号密码，验证码登录）
     * @Author king
     * @Date 2020/11/20
     */
    @RequestMapping(path = "/login/{loginType}", method = RequestMethod.POST)
    public ResultVO login(@PathVariable("loginType") String loginType, @RequestBody LoginUserPojo user) {
        return consumerServicesStanderImpl.login(loginType, user, user.getCode());
    }

    /**
     * @Describe 验证码
     * @Author king
     * @Date 2020/11/20
     */
    @RequestMapping(path = "/code/{phone}", method = RequestMethod.GET)
    public ResultVO getCode(@PathVariable(value = "phone") String phone) {
        return consumerServicesStanderImpl.getAndSaveCode(phone);
    }

    /**
     * @Describe 买票
     * @Author king
     * @Date 2020/12/1
     */
    @RequestMapping(path = "/ticket", method = RequestMethod.PUT)
    public ResultVO buyTicket(@RequestBody TicketRecodedPojo ticket, @RequestHeader String token) {
        //下单时间
        ticket.setBookTime(Calendar.getInstance().getTime());
        //哪一天可用
        ticket.setEffectiveTime(DateUtil.moreDate(1));
        return consumerServicesStanderImpl.BuyTicket(ticket, token);
    }

    /**
     * @Describe 剩余容量，票数
     * @Author king
     * @Date 2020/12/2
     */
    @RequestMapping(path = "/ticketInfo/{ticketName}", method = RequestMethod.GET)
    public ResultVO remainingNumber(@PathVariable String ticketName) {
        return consumerServicesStanderImpl.remainingNumber(0, ticketName);
    }

    /**
     * @Describe 拉取个人信息
     * @Author king
     * @Date 2020/11/30
     */
    @RequestMapping(path = "/personal", method = RequestMethod.GET)
    public ResultVOForType loadPersonalInfo(@RequestHeader String token) {
        return consumerServicesStanderImpl.loadPersonalInfo(token);
    }

    /**
     * @Describe 更新个人信息
     * @Author king
     * @Date 2020/11/30
     */
    @RequestMapping(value = "/personal", method = RequestMethod.POST)
    public ResultVO updatePersonal(@RequestBody LoginUserPojo user, @RequestHeader String token) {
        Claims claims = TokenUtil.parseJWT(token);
        user.setId(Long.parseLong(claims.get("uid").toString()));
        return consumerServicesStanderImpl.updatePersonalInfo(user);
    }

    /**
     * @Describe 更新头像
     * @Author king
     * @Date 2020/12/1
     */
    @RequestMapping(path = "/avatar", method = RequestMethod.POST)
    public ResultVO updateAvatar(MultipartFile avatar, String oldAvatar, String token) {
        return consumerServicesStanderImpl.upDateAvatar(avatar, oldAvatar, token);
    }

    /**
     * @Describe 验证密码
     * @Author king
     * @Date 2020/12/1
     */
    @RequestMapping(path = "/password", method = RequestMethod.GET)
    public ResultVO verifyPassword(String password, @RequestHeader String token) {
        return consumerServicesStanderImpl.verifyPassword(password, token);
    }


}
