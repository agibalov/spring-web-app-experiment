package me.loki2302;

import java.util.Date;

import me.loki2302.service.SomethingService;
import me.loki2302.service.BlogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {    
    @Autowired
    private SomethingService somethingService;
    
    @Autowired
    private BlogService blogService;
        
    @RequestMapping(value = "/", method = RequestMethod.GET)    
    public String index(Model model) {
        model.addAttribute("currentTime", new Date());
        model.addAttribute("whatServiceSays", somethingService.getSomething());
        model.addAttribute("categories", blogService.getCategories());
        return "index";
    }
    
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        return "test";
    }
}
