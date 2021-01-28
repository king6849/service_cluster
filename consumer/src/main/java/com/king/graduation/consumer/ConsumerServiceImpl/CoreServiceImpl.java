package com.king.graduation.consumer.ConsumerServiceImpl;

import Enties.ClassInfo;
import Enties.ClassSource;
import Enties.ClassTime;
import Enties.Train;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.king.graduation.consumer.ConsumerServices.CoreService;
import com.king.graduation.consumer.Mapper.ClassInfoMapper;
import com.king.graduation.consumer.Mapper.ClassSourceMapper;
import com.king.graduation.consumer.Mapper.ClassTimeMapper;
import com.king.graduation.consumer.Mapper.TrainMapper;
import com.king.graduation.consumer.Pojo.ClassKinds;
import com.king.graduation.consumer.Pojo.ClassRemainder;
import com.king.graduation.consumer.Pojo.LoadClassSourcePojo;
import com.king.graduation.consumer.utils.ResultVO;
import com.king.graduation.consumer.utils.objectConversion;
import org.springframework.stereotype.Service;
import utils.TokenUtil;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.*;

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

    @Resource
    private ClassTimeMapper classTimeMapper;

    private Random random = new Random();

    /**
     * @Describe 报名培训
     * @Author king
     * @Date 2021/1/25 - 下午8:53
     * @Params [token, classId]
     */
    @Override
    public ResultVO scheduleTraining(String token, long classId, long uTime) {
        //获取用户id
        long u_id = TokenUtil.parseJWTForKey(token, "id");
        //检查从重复报名问题
        Train isTraining = repeatRegistration(u_id);
        if (isTraining != null) {
            return ResultVO.getFailureResultVO("你已经报名了课程:" + classInfoMapper.selectById(isTraining.getIId()).getName());
        }
        //先检查学员报名哪一个班种，看看这个班种还有没有剩余人数可以报名
        //并且随机分配教练
        ClassInfo iid = checkClassRemainder(classId, uTime);
        if (iid == null) {
            return ResultVO.getFailureResultVO("人数已满");
        }

        Train train = new Train();
        train.setMakeTime(new Date(System.currentTimeMillis()));
        train.setCId(classId);
        train.setUTime(uTime);
        train.setIId(iid.getIId());
        train.setMembers(u_id);
        if (trainMapper.insertTrainRecord(train) > 0) {
            //更新可报名人数
            if (updateCapacity(uTime, iid.getId())) {
                return ResultVO.getSuccessResultVO("报名成功，请留意相关通知");
            }
        }
        return ResultVO.getFailureResultVO("报名失败，请稍后重试");
    }

    /** 
     * @return
     * @Describe 检测重复报名问题
     * @Author king
     * @Date 2021/1/28 - 下午12:14
     * @Params [uId]
     */
    private Train repeatRegistration(long uId) {
        EntityWrapper<Train> trainEntityWrapper = new EntityWrapper<>();
        trainEntityWrapper.eq("members", uId)
                .and()
                .eq("status", 1);
        List<Train> trains = trainMapper.selectList(trainEntityWrapper);
        if (trains.size() == 0) {
            return null;
        }
        return trainMapper.selectList(trainEntityWrapper).get(0);
    }

    /**
     * @Describe 检查可报名人数
     * @Author king
     * @Date 2021/1/27 - 下午7:36
     * @Params [classId, uTime]
     */
    public ClassInfo checkClassRemainder(long classId, long uTime) {
        List<ClassInfo> classInfoList = classInfoMapper.checkRemainder(classId, uTime);
        int size = classInfoList.size();
        if (size == 1) {
            return classInfoList.get(0);
        } else if (size > 1) {
            return classInfoList.get(random.nextInt(size));
        }
        return null;
    }

    /**
     * @Describe 更新班级剩余容量
     * @Author king
     * @Date 2021/1/26 - 下午2:26
     * @Params []
     */
    private boolean updateCapacity(long uTime, long classInfoTableId) {
        int status = classInfoMapper.updateRemainder(uTime, classInfoTableId);
        return status > 0;
    }


    /**
     * @Describe 加载开设的班级
     * @Author king
     * @Date 2021/1/25 - 下午9:15
     * @Params []
     */
    @Override
    public ResultVO trainClassInfoList() {
        EntityWrapper<ClassSource> entityWrapper = new EntityWrapper<>();
        entityWrapper.orderBy("id");
        //基本课程班级信息
        List<ClassSource> classSourceList = classSourceMapper.selectList(entityWrapper);
        //ClassSource对象转换一下 =>>>LoadClassSourcePojo
        List<LoadClassSourcePojo> loadClassSourcePojos = new LinkedList<>();
        for (ClassSource classSource : classSourceList) {
            loadClassSourcePojos.add((objectConversion.converToLoadClassSourcePojo(classSource)));
        }

        //每种课程上下午剩余可报名人数
        List<ClassInfo> classInfoList = classRemainderInfo();

        //加载学员可报名时间段，还剩remainder没有赋值　【{label: "上午", time: "10:30-12:00", id: 1, remainder: 1}，，，】
        List<ClassRemainder> classRemainderList = calculateRemainingCapacity();
        ClassRemainder amClassRemainder = classRemainderList.get(0);
        ClassRemainder pmClassRemainder = classRemainderList.get(1);

        int loadClassSourcePojo_size = loadClassSourcePojos.size();
        for (int i = 0; i < loadClassSourcePojo_size; i++) {
            LoadClassSourcePojo loadClassSourcePojo = loadClassSourcePojos.get(i);
            //拿到上下午剩余可报名人数
            ClassInfo classInfo = classInfoList.get(i);
            //给remainder赋值
            ClassRemainder am = amClassRemainderSetRemainder(amClassRemainder, classInfo.getRemainderAm());
            ClassRemainder pm = pmClassRemainderSetRemainder(pmClassRemainder, classInfo.getRemainderPm());

            loadClassSourcePojo.getRemainder().add(am);
            loadClassSourcePojo.getRemainder().add(pm);
        }

        return ResultVO.getSuccessResultVO("获取成功", loadClassSourcePojos);
    }

    /**
     * @Describe 转换成上午对象　{label: "上午", time: "10:30-12:00", id: 1, remainder: 0}
     * @Author king
     * @Date 2021/1/27 - 下午9:45
     * @Params [classRemainder, remainder]
     */
    private ClassRemainder amClassRemainderSetRemainder(ClassRemainder classRemainder, long remainder) {
        ClassRemainder classRemainderResult = new ClassRemainder();
        classRemainderResult.setTime(classRemainder.getTime());
        classRemainderResult.setId(classRemainder.getId());
        classRemainderResult.setClassLabel(classRemainder.getClassLabel());
        classRemainderResult.setRemainder(remainder);
        return classRemainderResult;
    }

    /**
     * @Describe 转换成下午午对象 {label: "上午", time: "10:30-12:00", id: 1, remainder: 0}
     * @Author king
     * @Date 2021/1/27 - 下午9:45
     * @Params [classRemainder, remainder]
     */
    private ClassRemainder pmClassRemainderSetRemainder(ClassRemainder classRemainder, long remainder) {
        ClassRemainder classRemainderResult = new ClassRemainder();
        classRemainderResult.setTime(classRemainder.getTime());
        classRemainderResult.setId(classRemainder.getId());
        classRemainderResult.setClassLabel(classRemainder.getClassLabel());
        classRemainderResult.setRemainder(remainder);
        return classRemainderResult;
    }

    //对象转换　转换结果是：[{label: "下午", time: "10:30-12:00", id: 1, remainder:0},,,]
    public List<ClassRemainder> calculateRemainingCapacity() {
        List<ClassRemainder> classRemainderList = new ArrayList<>();
        List<ClassTime> classTimes = classTimeMapper.allClassTimeInfo();
        for (ClassTime classTime : classTimes) {
            ClassRemainder classRemainder = objectConversion.converToClassRemainder(classTime);
            classRemainderList.add(classRemainder);
        }
        return classRemainderList;
    }

    /**
     * @Describe 加载上下午剩余可报名人数
     * @Author king
     * @Date 2021/1/26 - 下午8:27
     * @Params []这里主要是给ClassInfo对象的setRemainderAm，setRemainderPm
     * @Return []返回值主要是每种班级　上午与下午　剩余可报名人数
     */
    public List<ClassInfo> classRemainderInfo() {
        //每种课程ID
        List<Long> classType = classInfoMapper.classInfoList();
        List<ClassInfo> classKindsListResult = new ArrayList<>();
        for (Long classKinds : classType) {
            //每种课程开班，班级数量
            EntityWrapper<ClassInfo> classInfoEntityWrapper = new EntityWrapper<>();
            classInfoEntityWrapper.eq("c_id", classKinds);
            //每种班级，开了几个班，每个班的具体剩余可报名人数信息
            List<ClassInfo> classInfoList = classInfoMapper.selectList(classInfoEntityWrapper);
            long amRemainder = classInfoList.get(0).getRemainderAm();
            long pmRemainder = classInfoList.get(0).getRemainderPm();
            int size = classInfoList.size();
            if (classInfoList.size() > 1) {
                for (int i = 1; i < size; i++) {
                    ClassInfo classInfo = classInfoList.get(i);
                    amRemainder += classInfo.getRemainderAm();
                    pmRemainder += classInfo.getRemainderPm();
                }
            }
            //设置上下午的剩余可报名人数
            ClassInfo classInfo = new ClassInfo();
            classInfo.setRemainderAm(amRemainder);
            classInfo.setRemainderPm(pmRemainder);
            classKindsListResult.add(classInfo);
        }
        return classKindsListResult;
    }

    /**
     * @return [cId, numbers, members]
     * cid　班级id  numbers 开了几个班级　　，每种班级的人数上限
     * @Describe 每种班级开设的数量
     * @Author king
     * @Date 2021/1/26 - 下午1:12
     * @Params []
     */
    private List<ClassKinds> classNumbers() {
        List<ClassKinds> classKinds = classInfoMapper.classRemainder();
//        for (ClassRemainder classRemainder : classRemainders) {
//            classRemainder.setRemainder(classRemainder.getMembers() * classRemainder.getNumbers());
//        }
        return classKinds;
    }
}
