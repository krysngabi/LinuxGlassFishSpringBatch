package com.abovebytes.server.controller;

import com.abovebytes.server.utils.ShellCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@Controller
@CrossOrigin
public class HomeController {
    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/")
    public String index() {
       return "index.html";
    }
}
