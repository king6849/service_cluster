package com.king.graduation.backend_system.Service;

import org.springframework.web.multipart.MultipartFile;
import utils.ResultVO;

/**
 * @author king
 * @date 2021/1/2 - 22:22
 */
public interface VideoService {
    /**
     * @Describe 视屏资源列表
     * @Author king
     * @Date 2021/1/23 - 12:34
     * @Params []
     */
    ResultVO videoList(int startIndex, int size, int type);

    /**
     * 获取具体视屏资源信息
     *
     * @param id 视屏id
     * @return video对象
     */
    ResultVO getVideoInfo(long id);

    /**
     * @Describe 上传视屏
     * @Author king
     * @Date 2021/1/21 - 20:06
     */
    ResultVO uploadVideo(String token, String[] title, String describe, int type, MultipartFile[] file);

    /**
     * @Describe 更新观看次数
     * @Author king
     * @Date 2021/1/23 - 15:52
     * @Params [id]
     */
    void updateVideoViews(long id);

}
