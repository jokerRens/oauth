package com.joker.oss.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/member")
public class OssController {


    /**
     * 会员列表页面
     */
    @RequestMapping("/list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("member/list");
        return modelAndView;
    }

    @RequestMapping("/securedPage")
    public ModelAndView securedPage(){
        ModelAndView modelAndView = new ModelAndView("securedPage");
        return modelAndView;
    }



    @GetMapping("/test")
    public String test(){
        System.out.println("测试test");
        return "测试";
    }

    @GetMapping("/TestIndex")
    public ModelAndView Test() {
        System.out.println("hello");
        ModelAndView mv = new ModelAndView();
        mv.addObject("msg", "Hello,Springboot");
        mv.setViewName("indexs");
        return mv;
    }
}
