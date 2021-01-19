package com.king.graduation.backend_system.Controller;

import com.king.graduation.backend_system.Service.VideoService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import utils.ResultVO;

import javax.annotation.Resource;

/**
 * @author king
 * @date 2021/1/2 - 22:39
 */
@RestController
@RequestMapping(path = "/back")
public class VideoController {
    @Resource
    private VideoService videoService;

    @RequestMapping(path = "/video/{id}",method = RequestMethod.GET)
    public ResultVO videoInfo(@PathVariable long id){
        return videoService.getVideoInfo(id);
    }



}
