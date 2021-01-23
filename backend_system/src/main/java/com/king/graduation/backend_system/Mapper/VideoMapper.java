package com.king.graduation.backend_system.Mapper;

import Enties.VideoSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * @author king
 * @date 2021/1/2 - 22:13
 */
@Mapper
public interface VideoMapper extends BaseMapper<VideoSource> {

    /**
     * @Describe 更新观看次数
     * @Author king
     * @Date 2021/1/23 - 15:50
     * @Params [id]
     */
    @Update("update video_source set views=views+1 where id=#{id} ")
    int updateVideoViews(long id);
}
