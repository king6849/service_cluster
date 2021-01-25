package com.king.graduation.consumer.Controller;

import Pojo.TrainPojo;
import com.king.graduation.consumer.ConsumerServices.CoreService;
import com.king.graduation.consumer.utils.ResultVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author king
 * @date 2021/1/25 - 下午8:54
 */
@RestController
@RequestMapping(path = "/coreService")
public class CoreServiceController {

    @Resource
    private CoreService coreService;


    /**
     * @Describe 报名培训
     * @Author king
     * @Date 2021/1/25 - 下午8:57
     * @Params [token, id]
     */
    @RequestMapping(path = "/train",method = RequestMethod.PUT)
    public ResultVO orderTrain(@RequestHeader String token, @RequestBody TrainPojo train) {
        return coreService.scheduleTraining(token, train.getCid(), train.getTime());
    }

    /**
     * @Describe 加载开设的班级
     * @Author king
     * @Date 2021/1/25 - 下午9:17
     * @Params []
     */
    @GetMapping("/classInfo")
    public ResultVO trainClassInfoList() {
        return coreService.trainClassInfoList();
    }

}
