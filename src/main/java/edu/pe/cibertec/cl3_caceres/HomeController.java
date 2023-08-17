package edu.pe.cibertec.cl3_caceres;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "home";
    }
    @GetMapping("/nosotros")
    public String about() {
        return "about";
    }
}
