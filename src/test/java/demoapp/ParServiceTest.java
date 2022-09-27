package demoapp;

import demoapp.service.ParService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ParServiceTest {

    @Autowired
    ParService parService;

    // Probamos dos valores sencillos
    @Test
    public void dosNumerosParImpar() throws Exception {
        assertThat(parService.esPar(10)).isTrue();
        assertThat(parService.esPar(11)).isFalse();
    }

    // Probamos el 0, que según la Wikipedia es par
    // https://es.wikipedia.org/wiki/Paridad_del_cero
    @Test
    public void elCeroEsPar() throws Exception {
        assertThat(parService.esPar(0)).isTrue();
    }
}
