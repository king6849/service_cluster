package com.king.bishe.chat.MyFeign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("registryCenter")
public interface registryCenterFeign {

}
