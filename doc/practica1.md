# Documentación práctica 1 MADS

- Autor: Domingo Gallardo López   
- Fecha: 27/09/2022

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

La URL para probar la aplicación es: [localhost:8080/esPar](localhost:8080/esPar).

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

### Controlador

El controlador `ParController` define los métodos a ejecutar cuando se 
realizan las peticiones `GET` y `POST` a la URL `/esPar`.

La petición `GET` devuelve una plantilla con el formulario HTML, de forma
muy similar a la aplicación ejemplo inicial. 

La petición `POST` comprueba si ha habido algún error en el formulario, en
cuyo caso devuelve el propio formulario, y, si no, le pasa a la vista
`esParResult` el número y el booleano si es par o no. Para ello añade
al objeto `model` estos dos datos usando los atributos `numero` y `esPar`.

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

### Formulario

El formulario `formGetNum.html` pide al usuario el número. Es casi idéntico a 
la aplicación inicial. Solo cambia el objeto `numData` y el campo `numero`, 
que se corresponden con la nueva clase modelo `NumData`.

La clase `NumData` es la que contiene el campo `numero` que recoge en el
formulario. La anotación de validación `@Min(value = 0)` restringe los valores 
posibles del campo a números mayores o iguales a 0. El mensaje que se añade
en la anotación es el que se mostrará al usuario si se produce un error de 
validación.

```java
public class NumData {
    @Min(value = 0, message = "Debe ser mayor o igual que 0")
    Integer numero;
    
    // getters y setters
}
```


### Vista

El fichero `src/main/resources/esParResult.html` contiene la vista 
que muestra el resultado de la aplicación: el número en verde o en rojo 
dependiendo de si es par o impar.

El controlador le pasa a la vista los atributos `numero` y `esPar`. En
la vista se utiliza el atributo booleano para definir el estilo CSS de 
la cabecera `H1`. Usando la construcción de _Thymeleaf_ `th:style` se
hace un chequeo de la variable `esPar` y se define como estilo `color: green` 
o `color: red` dependiendo de su valor booleano. Para el texto del `H1` se
usa el valor del atributo `numero`.

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
    <p>(El número se muestra en verde si es par o en rojo si es impar)</p>
    <h1 th:style="${esPar} ? 'color: green;' : 'color: red'" th:text="${numero}" </h1>
</html>
```

### Tests

Los tests del servicio son muy sencillos. El primero comprueba dos números, uno
par y otro impar:

```java
@Test
public void dosNumerosParImpar() throws Exception {
    assertThat(parService.esPar(10)).isTrue();
    assertThat(parService.esPar(11)).isFalse();
}
```

Y el segundo comprueba el caso especial del número 0, que es par.

```java
// Probamos el 0, que según la Wikipedia es par
// https://es.wikipedia.org/wiki/Paridad_del_cero
@Test
public void elCeroEsPar() throws Exception {
    assertThat(parService.esPar(0)).isTrue();
}
```

Los tests del controller también son muy sencillos. Se pasa a la 
petición `POST` el parámetro con el número y se comprueba que en 
el HTML resultante aparece el propio número y el color correcto.

```java
@Test
public void postDevuelveNumeroVerdeCuandoEsPar() throws Exception {
    this.mockMvc.perform(post("/esPar")
            .param("numero", "10"))
            .andExpect(status().isOk())
            .andExpect(content().string(allOf(
                    containsString("10"),
                    containsString("green"))));
}
```

## Repositorios

- Repositorio GitHub: [https://github.com/mads-ua-22-23/springboot-demo-app-domingogallardo](https://github.com/mads-ua-22-23/springboot-demo-app-domingogallardo)
- Repositorio DockerHub: [https://hub.docker.com/r/domingogallardo/spring-boot-demoapp](https://hub.docker.com/r/domingogallardo/spring-boot-demoapp)
