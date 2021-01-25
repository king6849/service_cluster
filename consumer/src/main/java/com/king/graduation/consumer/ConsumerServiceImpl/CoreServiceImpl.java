package com.king.graduation.consumer.ConsumerServiceImpl;

import Enties.ClassInfo;
import Enties.ClassSource;
import Enties.Train;
import com.king.graduation.consumer.ConsumerServices.CoreService;
import com.king.graduation.consumer.Mapper.ClassInfoMapper;
import com.king.graduation.consumer.Mapper.ClassSourceMapper;
import com.king.graduation.consumer.Mapper.TrainMapper;
import com.king.graduation.consumer.utils.ResultVO;
import org.springframework.stereotype.Service;
import utils.TokenUtil;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author king
 * @date 2021/1/25 - 下午8:07
 */
@Service
public class CoreServiceImpl implements CoreService {

    @Resource
    private TrainMapper trainMapper;

    @Resource
    private ClassInfoMapper classInfoMapper;

    @Resource
    private ClassSourceMapper classSourceMapper;

    /**
     * @Describe 报名培训
     * @Author king
     * @Date 2021/1/25 - 下午8:53
     * @Params [token, classId]
     */
    @Override
    public ResultVO scheduleTraining(String token, long classId, long uTime) {
        String u_id = String.valueOf(TokenUtil.parseJWTForKey(token, "id"));
        Map<String, Object> query = new HashMap<>();
        query.put("c_id", classId);
        ClassInfo classInfo = null;
        try {
            classInfo = classInfoMapper.selectByMap(query).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.getFailureResultVO("报名失败，数据异常");
        }

        Train train = new Train();
        train.setMakeTime(new Date(System.currentTimeMillis()));
        train.setCId(classId);
        train.setIId(classInfo.getIId());
        train.setUTime(uTime);

        String members = train.getMembers();
        if (members == null || members.equals("")) {
            members = u_id;
        } else {
            members += "," + u_id;
        }
        train.setMembers(members);
        if (trainMapper.insert(train) > 0) {
            return ResultVO.getSuccessResultVO("报名成功，请留意相关通知");
        }
        return ResultVO.getFailureResultVO("报名失败，请稍后重试");
    }

    /**
     * @Describe 加载开设的班级
     * @Author king
     * @Date 2021/1/25 - 下午9:15
     * @Params []
     */
    @Override
    public ResultVO trainClassInfoList() {
        List<ClassSource> classInfoList = classSourceMapper.selectList(null);

        return ResultVO.getSuccessResultVO("获取成功", classInfoList);
    }
}
