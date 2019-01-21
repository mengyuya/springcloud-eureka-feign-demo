package com.example.serviceb.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

@FeignClient(value = "service-a")
public interface AFeign {

    @GetMapping("/getWho")
    String getWho();
}
