package com.zizhou.controller;

        import org.springframework.security.access.prepost.PreAuthorize;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RequestMethod;
        import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: NickXia
 * @date: 2020/8/7 18:15
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping(value = "/add",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('add')")
    public String add(){
        System.out.println("add....");
        return null;
    }

    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('delete')")
    public String delete(){
        System.out.println("delete....");
        return null;
    }

    @RequestMapping(value = "/update",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('update')")
    public String update(){
        System.out.println("update....");
        return null;
    }
}
