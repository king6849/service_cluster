package com.king.graduation.backend_system.Controller;

import com.king.graduation.backend_system.Service.VideoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    /**
     * @Describe 获取视屏列表
     * @Author king
     * @Date 2021/1/23 - 15:45
     * @Params [startIndex, size, type]
     */
    @RequestMapping(path = "/videoList/{type}", method = RequestMethod.GET)
    public ResultVO videoList(@RequestParam(defaultValue = "1") int startIndex, @RequestParam(defaultValue = "4") int size, @PathVariable int type) {
        return videoService.videoList(startIndex, size, type);
    }

    /**
     * @Describe 加载具体视屏信息
     * @Author king
     * @Date 2021/1/23 - 15:45
     * @Params [id]
     */
    @RequestMapping(path = "/video/{id}", method = RequestMethod.GET)
    public ResultVO videoInfo(@PathVariable long id) {
        return videoService.getVideoInfo(id);
    }

    /**
     * @Describe 上传视屏
     * @Author king
     * @Date 2021/1/23 - 15:45
     * @Params [token, title, describe, type, video]
     */
    @RequestMapping(path = "/video", method = RequestMethod.PUT)
    public ResultVO upLoadVideo(@RequestHeader String token, String[] title, String describe, int type, MultipartFile[] video) {
        return videoService.uploadVideo(token, title, describe, type, video);
    }

    /**
     * @Describe 更新观看次数
     * @Author king
     * @Date 2021/1/23 - 15:54
     * @Params [id]
     */
    @RequestMapping(path = "/video/{id}", method = RequestMethod.POST)
    public void updateVideoViews(@PathVariable long id) {
        videoService.updateVideoViews(id);
    }

}
