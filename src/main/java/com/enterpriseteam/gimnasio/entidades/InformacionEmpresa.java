
package com.enterpriseteam.gimnasio.entidades;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "informacionempresa")
public class InformacionEmpresa {
    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
