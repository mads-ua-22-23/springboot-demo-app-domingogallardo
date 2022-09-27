package demoapp.controller;

import demoapp.service.ParService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ParController {

    @Autowired
    private ParService service;

    @GetMapping("/esPar")
    // Hay que declarar un parámetro con el tipo usado en el modelo del formulario (UserData)
    public String saludoForm(NumData numData) {
        return "formGetNum";
    }

    @PostMapping("/esPar")
    public @ResponseBody String esPar(@ModelAttribute NumData numero, Model model) {
        String resultado = "El número " + numero.getNumero() + (service.esPar(numero.getNumero()) ? " es par" : " no es par");
        return resultado;
    }
}
