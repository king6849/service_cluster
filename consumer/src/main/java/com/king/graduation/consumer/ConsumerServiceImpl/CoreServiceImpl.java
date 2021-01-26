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
        String u_id = String.valueOf(TokenUtil.parseJWTForKey(token, "id"));
        //构建查询条件
        Map<String, Object> query = new HashMap<>();
        query.put("c_id", classId);
        ClassInfo classInfo = null;
        try {
            //获取开班信息
            classInfo = classInfoMapper.selectByMap(query).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.getFailureResultVO("报名失败，数据异常");
        }
        //问题1：每个课程可能会有多个教练来教，要提醒客户可报名剩余容量
        //问题2：如何随机给学员分配教练？
        Map<String, Object> param = new HashMap<>();
        param.put("c_id", classId);

        Train train = null;
        try {
            //获取已报名的报名信息
            train = trainMapper.selectByMap(param).get(0);
        } catch (Exception e) {
            train = new Train();
        }
        //教练id
        long iid = randomlyAssignedInstructor(classId);

        train.setMakeTime(new Date(System.currentTimeMillis()));
        train.setCId(classId);
        train.setIId(classInfo.getIId());
        train.setUTime(uTime);
        train.setIId(iid);
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
     * @Describe 更新班级剩余容量
     * @Author king
     * @Date 2021/1/26 - 下午2:26
     * @Params []
     */
    private boolean updateCapacity() {

        return false;
    }

    /**
     * @Describe 随机分配教练
     * @Author king
     * @Date 2021/1/26 - 下午12:55
     * @Params []
     */
    private long randomlyAssignedInstructor(long cId) {
        EntityWrapper<ClassInfo> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("c_id", cId)
                .and().gt("remainder", 0);
        List<ClassInfo> classInfoList = classInfoMapper.selectList(entityWrapper);
        int list_size = classInfoList.size();
        int index = 0;
        if (list_size > 1) {
            index = random.nextInt(list_size);
        }
        ClassInfo classInfo = classInfoList.get(index);
        return classInfo.getIId();
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

        //对象转换一下
        List<LoadClassSourcePojo> loadClassSourcePojos = new LinkedList<>();
        for (ClassSource classSource : classSourceList) {
            loadClassSourcePojos.add((objectConversion.converToLoadClassSourcePojo(classSource)));
        }

        List<ClassInfo> classInfoList = classRemainderInfo();

        //添加　【{label: "上午", time: "10:30-12:00", id: 1, remainder: 1}，，，】
        int loadClassSourcePojo_size = loadClassSourcePojos.size();
        List<ClassRemainder> classRemainderList = calculateRemainingCapacity();
        ClassRemainder amClassRemainder = classRemainderList.get(0);
        ClassRemainder pmClassRemainder = classRemainderList.get(1);
        for (int i = 0; i < loadClassSourcePojo_size; i++) {
            LoadClassSourcePojo loadClassSourcePojo = loadClassSourcePojos.get(i);
            ClassInfo classInfo = classInfoList.get(i);

            ClassRemainder am = amClassRemainderSetRemainder(amClassRemainder,classInfo.getRemainderAm());
            ClassRemainder pm = pmClassRemainderSetRemainder(pmClassRemainder,classInfo.getRemainderPm());


            loadClassSourcePojo.getRemainder().add(am);
            loadClassSourcePojo.getRemainder().add(pm);
        }

        return ResultVO.getSuccessResultVO("获取成功", loadClassSourcePojos);
    }

    private ClassRemainder amClassRemainderSetRemainder(ClassRemainder classRemainder,long remainder) {
        ClassRemainder classRemainderResult = new ClassRemainder();
        classRemainderResult.setTime(classRemainder.getTime());
        classRemainderResult.setId(classRemainder.getId());
        classRemainderResult.setClassLabel(classRemainder.getClassLabel());
        classRemainderResult.setRemainder(remainder);
        return classRemainderResult;
    }

    private ClassRemainder pmClassRemainderSetRemainder(ClassRemainder classRemainder,long remainder) {
        ClassRemainder classRemainderResult = new ClassRemainder();
        classRemainderResult.setTime(classRemainder.getTime());
        classRemainderResult.setId(classRemainder.getId());
        classRemainderResult.setClassLabel(classRemainder.getClassLabel());
        classRemainderResult.setRemainder(remainder);
        return classRemainderResult;
    }

    //对象转换　转换结果是：[{label: "上午", time: "10:30-12:00", id: 1, remainder:0},,,]
    public List<ClassRemainder> calculateRemainingCapacity() {
        List<ClassRemainder> classRemainderList = new ArrayList<>();
        List<ClassTime> classTimes = classTimeMapper.allClassTimeInfo();
        System.out.println("classTimes " + classTimes);
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
     * @Params []
     * @Return []
     */
    public List<ClassInfo> classRemainderInfo() {

        List<ClassInfo> classInfoList = classInfoMapper.classInfoList();
        //开了几个班级
        List<ClassKinds> classKindsList = classNumbers();
        int classInfoList_size = classInfoList.size();
        for (int i = 0; i < classInfoList_size; i++) {
            ClassInfo classInfo = classInfoList.get(i);
            System.out.println(classInfo);
            ClassKinds classKinds = classKindsList.get(i);
            //开了几个班
            long classNumbers = classKinds.getNumbers();
            long remainder = classNumbers * classKinds.getMembers();
            //设置上下午的剩余可报名人数
            classInfo.setRemainderAm(remainder);
            classInfo.setRemainderPm(remainder);
        }
        return classInfoList;
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
