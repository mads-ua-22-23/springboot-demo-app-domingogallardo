package demoapp.service;

import org.springframework.stereotype.Service;

@Service
public class ParService {
    public boolean esPar(int num) {
        return (num % 2) == 0;
    }
}
