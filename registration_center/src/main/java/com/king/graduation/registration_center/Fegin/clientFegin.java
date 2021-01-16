package com.king.graduation.registration_center.Fegin;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author king
 * @date 2020/11/23
 */
@FeignClient("consumer")
public interface clientFegin {


}
