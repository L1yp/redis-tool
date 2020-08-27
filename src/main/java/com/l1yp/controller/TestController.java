package com.l1yp.controller;

import com.l1yp.model.TestParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Lyp
 * @Date 2020-08-27
 * @Email l1yp@qq.com
 */
@RestController
@Slf4j
public class TestController {

    @PostMapping("/test")
    public TestParam test(@RequestBody TestParam param) {
        log.info("param: {}", param);
        return param;
    }


}
