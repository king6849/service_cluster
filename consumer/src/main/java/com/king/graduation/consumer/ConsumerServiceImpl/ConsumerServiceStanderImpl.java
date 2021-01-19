package com.king.graduation.consumer.ConsumerServiceImpl;

import com.alibaba.fastjson.JSONObject;
import com.king.graduation.consumer.Config.RestTemplateConfig;
import com.king.graduation.consumer.ConsumerServices.ConsumerServices;
import com.king.graduation.consumer.Entity.TicketType;
import com.king.graduation.consumer.Entity.User;
import com.king.graduation.consumer.Mapper.TicketTypeMapperPlus;
import com.king.graduation.consumer.Mapper.consumerMapper;
import com.king.graduation.consumer.Mapper.consumerMapperPlus;
import com.king.graduation.consumer.Pojo.LoginUserPojo;
import com.king.graduation.consumer.Pojo.TicketRecodedPojo;
import com.king.graduation.consumer.utils.*;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @Author king
 * @date 2020/11/19
 */
@Service(value = "consumerServiceStanderImpl")
@Slf4j
public class ConsumerServiceStanderImpl implements ConsumerServices {
    @Autowired
    private consumerMapper consumerMapper;

    @Autowired
    private consumerMapperPlus consumerMapperPlus;

    @Autowired
    private TicketTypeMapperPlus ticketTypeMapperPlus;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RestTemplateConfig restTemplateConfig;

    @Autowired
    private ResultVOForType resultVOForType;

    private String host = "http://localhost:8081";

    private String fileRootPath = "F:\\idea\\projects\\bishe\\images";

    private String avatarBasePath = fileRootPath + "\\avatar";

    private File avatarBasePathFile = new File(avatarBasePath);

    @Override
    public ResultVO register(String phone, String password) {
        if (consumerMapper.register(phone, password) >= 1) {
            return ResultVO.getSuccessResultVO("注册成功");
        }
        return ResultVO.getFailureResultVO("注册异常");
    }

    /**
     * @Describe 微信登录，手机号登录，账号密码登录
     * @Author king
     * @Date 2020/11/19
     */

    @Override
    public ResultVO login(String loginType, User user, String code) {
//        账号密码登录
        User accountLogin = consumerMapper.accountLogin(user.getPhone());
        if (loginType.equals("accountLogin")) {
            if (accountLogin == null) {
                return ResultVO.getFailureResultVO("不存在该账号");
            } else if (!accountLogin.getPassword().equals(user.getPassword())) {
                return ResultVO.getFailureResultVO("账号密码错误");
            } else {
                return ResultVO.getSuccessResultVO("登录成功", TokenUtil.getToken(accountLogin));
            }
        } else if (loginType.equals("phoneLogin")) {
            //手机验证码登录
            String key = user.getPhone() + "_code";
            String userCode = redisUtil.stringGet(key);
            if (!verifyCode(code, userCode)) {
                return ResultVO.getFailureResultVO("验证码错误");
            }
            redisUtil.del(key);
            return ResultVO.getSuccessResultVO("登录成功", TokenUtil.getToken(accountLogin));
        } else {
            //微信扫码登录
            return weChatLogin(code);
        }
    }

    public boolean verifyCode(String receiveCode, String serviceCode) {
        receiveCode = (receiveCode.trim()).toLowerCase();
        serviceCode = (serviceCode.trim()).toLowerCase();
        log.debug("receiveCode:{}", receiveCode);
        log.debug("serviceCode:{}", serviceCode);
        return receiveCode.equals(serviceCode);
    }


    /**
     * @Describe 微信扫码登录
     * @Author king
     * @Date 2020/11/20
     */
    public ResultVO weChatLogin(String code) throws RuntimeException {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        String appid = "wxe98b8264fdd0e52a";
        String secret = "10e414d4adbb3f5ca8f4d64047a0aac7";
        String js_code = code;
        String grant_type = "authorization_code";
        String codereslut = restTemplateConfig.getForObject(url + "?appid=" + appid + "&secret=" + secret + "&js_code=" + js_code + "&grant_type=" + grant_type, String.class);
        JSONObject jsonObject = JSONObject.parseObject(codereslut);
        //解析出session_key，openid
        String session_key = jsonObject.getString("session_key");
        String openid = jsonObject.getString("openid");
        //生成token
        String token = TokenUtil.getToken(openid, session_key);
        isExitOpenId(openid);
        //返回结果
        return ResultVO.getSuccessResultVO("登陆成功", token);
    }

    public void isExitOpenId(String openid) {
        long id = consumerMapper.exitOpenId(openid);
        //user表中没有该openid
        if (id <= 0) {
            consumerMapper.weChatLogin(openid);
        }
    }

    /**
     * @Describe 获取验证码
     * @Author king
     * @Date 2020/11/23
     */
    @Override
    public ResultVO getAndSaveCode(String phone) {
        String code = GenerateCode.getSingleCode();
        redisUtil.stringSet(phone + "_code", code);
        return ResultVO.getSuccessResultVO("获取成功", code);
    }

    /**
     * @Describe 买票
     * @Author king
     * @Date 2020/11/23
     */
    @Override
    @Transactional
    public ResultVO BuyTicket(TicketRecodedPojo ticket, String token) {
        if (ticket.getTicketName() == null) {
            return ResultVO.getFailureResultVO("数据异常,请重新操作");
        }
        int ticketNumbers = consumerMapper.ticketNumbers(ticket.getTId(), ticket.getTicketName());
        if (ticketNumbers - ticket.getNumbers() < 0) {
            return ResultVO.getFailureResultVO("库存不足,无法购买您所需的" + ticket.getNumbers() + "张票");
        }
        TicketType ticketType = consumerMapper.getTicketTypeId(ticket.getTicketName());

        ticket.setTicketType(ticketType);
        ticket.setTId(ticketType.getTicketId());
        ticket.setUId(TokenUtil.parseJWTForKey(token, "uid"));
        ticket.setTotalPrice(ticket.getTicketPrice() * ticket.getNumbers());
        if (consumerMapper.buyATicket(ticket) >= 1) {
            consumerMapper.desTicketNumbers(ticket);
            return ResultVO.getSuccessResultVO("购买成功");
        }
        return ResultVO.getFailureResultVO("购买失败，发生未知异常");
    }

    @Override
    public ResultVO remainingNumber(long id, String ticketName) {
        return ResultVO.getSuccessResultVO("获取成功", consumerMapper.ticketTypeInfo(id, ticketName));
    }

    /**
     * @Describe 拉取个人信息
     * @Author king
     * @Date 2020/11/30
     */
    @Override
    public ResultVOForType loadPersonalInfo(String token) {
        Claims claims = TokenUtil.parseJWT(token);
        int uid = (int) claims.get("uid");
        LoginUserPojo userPojo = consumerMapper.loadPersonalInfo(uid);
        if (userPojo == null) {
            return resultVOForType.getFailureResultVO("不存在该用户");
        }
        return resultVOForType.getSuccessResultVO("获取个人信息成功", userPojo);
    }

    /**
     * @Describe 更新个人信息
     * @Author king
     * @Date 2020/11/30
     */
    @Override
    public ResultVO updatePersonalInfo(User user) {
        if (user.getId() == 0) {
            return ResultVO.getFailureResultVO("用户数据异常");
        }
        if (consumerMapper.updatePersonal(user) <= 0) {
            return ResultVO.getFailureResultVO("更新失败");
        }
        return ResultVO.getSuccessResultVO("更新成功");
    }


    /**
     * @Describe 更新头像
     * @Author king
     * @Date 2020/12/1
     */
    @Override
    public ResultVO upDateAvatar(MultipartFile avatar, String oldAvatar, String token) {
        String originalFilename = avatar.getOriginalFilename();
        assert originalFilename != null;
        //后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //新头像的全名
        String saveAvatarName = RandomStringUtils.randomAlphanumeric(16) + suffix;

        String avatarFullPathName = host + "/avatar/" + saveAvatarName;
        try {
            avatar.transferTo(new File(avatarBasePathFile, saveAvatarName));
            //更新数据库的头像路径
            consumerMapper.updateAvatar(avatarFullPathName, TokenUtil.parseJWTForKey(token, "uid"));
            //删除旧的头像
            String oldAva = oldAvatar.substring(oldAvatar.lastIndexOf("/") + 1);
            if (!oldAva.equals("defaultAvatar.jpg")) {
                System.out.println("删除旧的头像 " + oldAva);
                new File(avatarBasePath + "\\" + oldAva).delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultVO.getSuccessResultVO("上传成功", avatarFullPathName);
    }

    /**
     * @Describe 验证密码
     * @Author king
     * @Date 2020/12/1
     */
    @Override
    public ResultVO verifyPassword(String password, String token) {
        System.out.println("uid is " + TokenUtil.parseJWTForKey(token, "uid"));
        String oldPassword = consumerMapper.getPassword(TokenUtil.parseJWTForKey(token, "uid"));
        System.out.println("oldPassword is " + oldPassword);
        if (!password.equals(oldPassword)) {
            return ResultVO.getFailureResultVO("密码不正确");
        }
        return ResultVO.getSuccessResultVO("验证通过");
    }
}