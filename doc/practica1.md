# Documentación práctica 1 MADS

Autor: Domingo Gallardo Lóepz

## Funcionalidad implementada

Se ha implementado una sencilla aplicación Spring Boot con un formulario
en el que se introduce un número mayor o igual que 0 y comprueba si
el número es par o impar.

Si el número introducido es par se muestra en color verde y si es impar 
en color rojo.

Para probar la aplicación hay que lanzarla con el comando:

```
./mvnw spring-boot:run
```

La URL para probar la aplicación es: <localhost:8080/esPar>.

## Implementación

La aplicación utiliza la misma estructura que hemos visto en la 
aplicación inicial. Utilizamos Spring Boot para definir una capa de
servicio en la que se define la lógica de negocio (comprobar
si un número es par), una capa de _controller_ en la que se definen los _end points_
de la aplicación web y unas plantillas _Thymeleaf_ para implementar el formulario y 
la vista resultante. 

También se han añadido unos tests que comprueban el funcionamiento correcto
del servicio y de los controllers.

Explicamos a continuación todo esto con algo más de detalle.

### Servicio

La clase de servicio `ParService` tiene un único método que es el encargado
de comprobar si un número es par. Se le pasa un número `int` y se 
devuelve un `boolean` que indica si el número recibido es o no par.

La implementación es muy sencilla: se comprueba si el módulo de la división
por 2 es 0.

```java
@Service
public class ParService { 
    public boolean esPar(int num) {
        return (num % 2) == 0;
    }
}
```

### Controller


```java
@Controller
public class ParController {

    @Autowired
    private ParService service;
    
    // @GetMapping("/esPar")
    // ...
    
    @PostMapping("/esPar")
    public String esPar(@ModelAttribute @Valid NumData numero, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "formGetNum";
        } else {
            model.addAttribute("numero", numero.getNumero());
            model.addAttribute("esPar", service.esPar(numero.getNumero()));
            return "esParResult";
        }
    }
}
```

### Modelo y formulario

```java
public class NumData {
    @Min(value = 0, message = "Debe ser mayor o igual que 0")
    Integer numero;
    
    // getters y setters
}
```

El formulario HTML es idéntico a la aplicación inicial. Solo cambia 
el objeto `numData` y el campo `numero`, para adaptarlo a la nueva
clase modelo.

### Vista

El fichero `src/main/resources/esParResult.html` contiene la vista 
que muestra el resultado.

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
    <p>(El número se muestra en verde si es par o en rojo si es impar)</p>
    <h1 th:style="${esPar} ? 'color: green;' : 'color: red'" th:text="${numero}" </h1>
</html>
```

### Tests

Tests del servicio:

```java
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
```

Tests del controller:

```java
@SpringBootTest
@AutoConfigureMockMvc
public class ParControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void postDevuelveNumeroVerdeCuandoEsPar() throws Exception {
        this.mockMvc.perform(post("/esPar")
                .param("numero", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string(allOf(
                        containsString("10"),
                        containsString("green"))));
    }
    
    // Otros tests similares
}
```

## Repositorios


