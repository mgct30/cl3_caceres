package edu.pe.cibertec.cl3_caceres;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalController {
    
    @ModelAttribute("year")
    public int addYear(){
        return 2023; 
    }
}
