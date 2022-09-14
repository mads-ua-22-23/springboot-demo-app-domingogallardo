package demoapp.service;

import org.springframework.stereotype.Service;

@Service
public class SaludoService {
    public String saluda(String nombre) {
        return "Buenos días " + nombre;
    }
}