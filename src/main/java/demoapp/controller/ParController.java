package demoapp.controller;

import demoapp.service.ParService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ParController {

    @Autowired
    private ParService service;

    @GetMapping("/esPar/{numero}")
    public @ResponseBody String esPar(@PathVariable(value="numero") String numero) {
        Integer num = new Integer(numero);
        String resultado = "El número " + numero + (service.esPar(num) ? " es par" : " no es par");
        return resultado;
    }
}
