package com.king.graduation.backend_system.ServicesImpl;

import Enties.User;
import Enties.VideoSource;
import Enties.VideoType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.king.graduation.backend_system.Mapper.UserMapper;
import com.king.graduation.backend_system.Mapper.VideoMapper;
import com.king.graduation.backend_system.Mapper.VideoTypeMapper;
import com.king.graduation.backend_system.Service.VideoService;
import com.king.graduation.backend_system.Utils.VideoUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import utils.DateUtil;
import utils.GenerateCode;
import utils.ResultVO;
import utils.TokenUtil;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author king
 * @date 2021/1/2 - 22:35
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Resource
    private VideoMapper videoMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private VideoTypeMapper videoTypeMapper;

    private int port = 9091;

    private String host = "http://localhost";

    private String hostAddr = host + ":" + port;

    private SimpleDateFormat simpleFormatter = DateUtil.getSimpleDateFormat();

    /**
     * @Describe 获取视屏列表
     * @Author king
     * @Date 2021/1/23 - 13:38
     * @Params [startIndex, size, type]
     */
    @Override
    public ResultVO videoList(int startIndex, int size, int type) {
        QueryWrapper<VideoSource> queryWrapper = new QueryWrapper<VideoSource>();
        queryWrapper.eq("type", type);
        Page<VideoSource> page = new Page<>(startIndex, size);
        IPage<VideoSource> videoSources = videoMapper.selectPage(page, queryWrapper);
        return ResultVO.getSuccessResultVO("获取成功", videoSources.getRecords());
    }


    @Override
    public ResultVO getVideoInfo(long id) {

        VideoSource videoSource = videoMapper.selectById(id);
        if (videoSource == null) {
            return ResultVO.getFailureResultVO("该视频不存在");
        }
        return ResultVO.getSuccessResultVO("获取视屏资源成功", videoSource);
    }


    @Override
    public ResultVO uploadVideo(String token, String[] title, String describe, int type, MultipartFile[] file) {
        try {
            doUploadVideo(token, title, describe, type, file);
        } catch (IOException e) {
            return ResultVO.getFailureResultVO("发生未知错误，请重试");
        }
        return ResultVO.getSuccessResultVO("正在上传中...");
    }

    @Async
    void doUploadVideo(String token, String[] title, String describe, int type, MultipartFile[] file) throws IOException {
        for (int i = 0; i < file.length; i++) {
            long id = TokenUtil.parseJWTForKey(token, "id");
            User user = userMapper.selectById(id);

            MultipartFile videoTempFile = file[i];
            //唯一编码
            String videoCodeName = GenerateCode.getVideoCode();
            //视屏类型对应的路径
            String path = VideoUtil.getVideoPath(type);
            //视屏名字与后缀
            String videoRealName = videoCodeName + ".mp4";
            videoTempFile.transferTo(new File(path, videoRealName));
            File videoFile = new File(path, videoRealName);
            //时长
            long duration = VideoUtil.getVideoDuration(videoFile);
            //封面名字，后缀
            String imgName = videoCodeName + ".jpg";
            //执行截取封面
            VideoUtil.getVideoPic(videoFile, VideoUtil.getVideoImgPath(type) + imgName);

            VideoType videoType = videoTypeMapper.selectById(type);
            String video_url = hostAddr + "/video/" + videoType.getName() + "/" + videoRealName;
            String img_url = hostAddr + "/video/" + videoType.getName() + "/img/" + imgName;
            videoMapper.insert(new VideoSource(title[i], video_url, img_url, duration, user.getNickName(), user.getAvatar(), new Date(), type, describe, videoCodeName));
        }
    }

    @Override
    public void updateVideoViews(long id) {
        videoMapper.updateVideoViews(id);
    }


}
