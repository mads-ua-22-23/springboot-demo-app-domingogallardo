package demoapp.controller;

import javax.validation.constraints.Positive;

public class NumData {
    @Positive
    Integer numero;

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }
}
