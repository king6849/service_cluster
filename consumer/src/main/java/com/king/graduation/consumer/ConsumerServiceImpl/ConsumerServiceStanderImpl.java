package com.king.graduation.consumer.ConsumerServiceImpl;

import Enties.Pool;
import Enties.TicketRecord;
import Enties.TicketType;
import Enties.User;
import com.alibaba.fastjson.JSONObject;
import com.king.graduation.consumer.Config.RestTemplateConfig;
import com.king.graduation.consumer.ConsumerServices.ConsumerServices;
import com.king.graduation.consumer.Mapper.*;
import com.king.graduation.consumer.Pojo.Adult;
import com.king.graduation.consumer.Pojo.BuyTicketPojo;
import com.king.graduation.consumer.Pojo.LoginUserPojo;
import com.king.graduation.consumer.Pojo.Minor;
import com.king.graduation.consumer.utils.RedisUtil;
import com.king.graduation.consumer.utils.ResultVO;
import com.king.graduation.consumer.utils.ResultVOForType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import utils.GenerateCode;
import utils.TokenUtil;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * @Author king
 * @date 2020/11/19
 */
@Service(value = "consumerServiceStanderImpl")
@Slf4j
public class ConsumerServiceStanderImpl implements ConsumerServices {
    @Autowired
    private consumerMapper consumerMapper;

    @Resource
    private TicketRecordMapper ticketRecordMapper;

    @Resource
    private poolMapper poolMapper;

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

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private String host = "http://localhost:8081";

    private String fileRootPath = "F:/idea/projects/bishe/images";

    private String avatarBasePath = fileRootPath + "/avatar";

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
        if ("accountLogin".equals(loginType)) {
            if (accountLogin == null) {
                return ResultVO.getFailureResultVO("不存在该账号");
            } else if (!accountLogin.getPassword().equals(user.getPassword())) {
                return ResultVO.getFailureResultVO("账号密码错误");
            } else {
                return ResultVO.getSuccessResultVO("登录成功", TokenUtil.getToken(accountLogin));
            }
        } else if ("phoneLogin".equals(loginType)) {
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
    public ResultVO BuyTicket(BuyTicketPojo ticket, String token) {
        //生成订单号
        String orderNo = GenerateCode.getOrderIdByTime();
        //购买时间
        Date buyDate = new Date(new java.util.Date().getTime());
        //用户id
        long userId = TokenUtil.parseJWTForKey(token, "id");
        //检查库存与所买票数
        ResultVO resultVO = checkTicketReserve(ticket.getAdult().getNumbers(), ticket.getMinor().getNumbers());
        assert resultVO != null;
        if (resultVO != null) {
            return resultVO;
        }
        //执行添加记录
        if (executionRecord(ticket.getAdult(), ticket.getMinor(), orderNo, buyDate, userId)) {
            return ResultVO.getSuccessResultVO("购买成功");
        }
        return ResultVO.getFailureResultVO("购买失败，发生未知异常");
    }

    private boolean executionRecord(Adult adult, Minor minor, String orderNo, java.sql.Date buyDate, long userId) {
        boolean adultBoolean = false;
        boolean minorBoolean = false;
        if (adult.getNumbers() > 0) {
            if (recordAdultTicket(adult, orderNo, buyDate, userId)) {
                adultBoolean = true;
            }
        } else {
            adultBoolean = true;
        }
        if (minor.getNumbers() > 0) {
            if (recordMinorTicket(minor, orderNo, buyDate, userId)) {
                minorBoolean = true;
            }
        } else {
            minorBoolean = true;
        }

        return adultBoolean && minorBoolean;
    }

    /**
     * @Describe 添加成人票记录
     * @Author king
     * @Date 2021/1/23 - 17:27
     * @Params [adult, orderNo, buyDate, userId]
     */
    private boolean recordAdultTicket(Adult adult, String orderNo, java.sql.Date buyDate, long userId) {
        TicketRecord ticketRecord = null;
        try {
            Date effectiveDate = new java.sql.Date(simpleDateFormat.parse(adult.getDate()).getTime());
            ticketRecord = ticketRecordTemplate(orderNo, adult.getNumbers(), adult.getTicketPrice(), buyDate, effectiveDate, userId, adult.getId());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (ticketRecordMapper.insert(ticketRecord) > 0) {
            //更新票数
            decreaseTicketNumbers(adult.getNumbers(), adult.getPool());
            return true;
        }
        return false;
    }

    /**
     * @Describe 添加未成人票记录
     * @Author king
     * @Date 2021/1/23 - 17:27
     * @Params [adult, orderNo, buyDate, userId]
     */
    private boolean recordMinorTicket(Minor minor, String orderNo, Date buyDate, long userId) {
        TicketRecord ticketRecord = null;
        try {
            Date effectiveDate = new java.sql.Date(simpleDateFormat.parse(minor.getDate()).getTime());
            ticketRecord = ticketRecordTemplate(orderNo, minor.getNumbers(), minor.getTicketPrice(), buyDate, effectiveDate, userId, minor.getId());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (ticketRecordMapper.insert(ticketRecord) > 0) {
            //更新票数
            decreaseTicketNumbers(minor.getNumbers(), minor.getPool());
            return true;
        }
        return false;
    }

    /**
     * @Describe 购票记录模板
     * @Author king
     * @Date 2021/1/23 - 17:32
     * @Params [orderNo, numbers, price, buyDate, userId, type]
     */
    private TicketRecord ticketRecordTemplate(String orderNo, int numbers, double price, java.sql.Date buyDate, java.sql.Date effectiveDate, long userId, long type) {
        TicketRecord ticketRecord = new TicketRecord();
        ticketRecord.setOrderNumber(orderNo);
        ticketRecord.setNumbers(numbers);
        ticketRecord.setMoney(numbers * price);
        ticketRecord.setBookTime(buyDate);
        ticketRecord.setEffectiveTime(effectiveDate);
        ticketRecord.setUId(userId);
        ticketRecord.setTId(type);
        return ticketRecord;
    }

    /**
     * @Describe 检查票数
     * @Author king
     * @Date 2021/1/23 - 17:10
     * @Params [adultNumbers, minorNumbers]
     */
    private ResultVO checkTicketReserve(int adultNumbers, int minorNumbers) {
        if (adultNumbers > 0) {
            Pool adult = poolMapper.selectById(1);
            if (adult.getTotalTicket() - adultNumbers < 0) {
                return ResultVO.getFailureResultVO("成人票库存不足");
            }
        }
        if (minorNumbers > 0) {
            Pool minor = poolMapper.selectById(2);
            if (minor.getTotalTicket() - minorNumbers < 0) {
                return ResultVO.getFailureResultVO("未成人成人票库存不足");
            }
        }
        return null;
    }

    /**
     * @Describe 更新票数
     * @Author king
     * @Date 2021/1/23 - 17:38
     * @Params [numbers, type]
     */
    private int decreaseTicketNumbers(int numbers, long id) {
        return poolMapper.decreaseTicketNumbers(numbers, id);
    }

    /**
     * @Describe 加载购票信息
     * @Author king
     * @Date 2021/1/23 - 20:16
     * @Params []
     */
    @Override
    public ResultVO loadTicketInfo() {
        List<Pool> pools = poolMapper.poolInfoList();
        return ResultVO.getSuccessResultVO("获取成功", pools);
    }

    /**
     * @Describe 加载购物车基本信息
     * @Author king
     * @Date 2021/1/23 - 22:20
     * @Params []
     */
    @Override
    public ResultVO loadShoppingCarInfo() {
        List<TicketType> ticketTypes = ticketTypeMapperPlus.loadShoppingCarInfo();
        return ResultVO.getSuccessResultVO("获取成功", ticketTypes);
    }

    /**
     * @Describe 拉取个人信息
     * @Author king
     * @Date 2020/11/30
     */
    @Override
    public ResultVOForType loadPersonalInfo(String token) {
        long id = TokenUtil.parseJWTForKey(token, "id");
        LoginUserPojo userPojo = consumerMapper.loadPersonalInfo(id);
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
                new File(avatarBasePath + "/" + oldAva).delete();
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