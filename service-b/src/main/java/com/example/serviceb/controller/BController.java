package com.example.serviceb.controller;

import com.example.serviceb.feign.AFeign;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class BController {
    @Resource
    AFeign aFeign;

    @GetMapping("/getAll")
    public String getAll() {
        String who = aFeign.getWho();
        String doingWhat = getWhatDoing();
        return who+doingWhat;
    }

    @GetMapping("/getWhatDoing")
    public String getWhatDoing() {
        return "正在工作";
    }
}
