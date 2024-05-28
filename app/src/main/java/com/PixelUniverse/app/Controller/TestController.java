package com.PixelUniverse.app.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/get")
    public String GetTest(@RequestParam(value = "name",defaultValue = "World") String name){
        return String.format("Hello %s!",name);
    }
}
