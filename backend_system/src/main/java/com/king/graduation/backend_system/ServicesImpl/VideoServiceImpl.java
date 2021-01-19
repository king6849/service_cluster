package com.king.graduation.backend_system.ServicesImpl;

import com.king.graduation.backend_system.Enties.Video;
import com.king.graduation.backend_system.Mapper.VideoMapper;
import com.king.graduation.backend_system.Service.VideoService;
import org.springframework.stereotype.Service;
import utils.ResultVO;

import javax.annotation.Resource;

/**
 * @author king
 * @date 2021/1/2 - 22:35
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Resource
    private VideoMapper videoMapper;

    @Override
    public ResultVO getVideoInfo(long id) {
        Video video = videoMapper.selectById(id);
        if (video == null) {
            return ResultVO.getFailureResultVO("该视频不存在");
        }
        return ResultVO.getSuccessResultVO("获取视屏资源成功", video);
    }
}
