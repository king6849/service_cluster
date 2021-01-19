package com.king.graduation.backend_system.Service;

import utils.ResultVO;

/**
 * @author king
 * @date 2021/1/2 - 22:22
 */
public interface VideoService {
    /**
     * 获取视屏资源信息
     * @param id 视屏id
     * @return video对象
     */
    ResultVO getVideoInfo(long id);

}
