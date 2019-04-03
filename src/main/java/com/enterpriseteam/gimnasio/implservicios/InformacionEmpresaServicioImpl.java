/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enterpriseteam.gimnasio.implservicios;

import com.enterpriseteam.gimnasio.entidades.InformacionEmpresa;
import com.enterpriseteam.gimnasio.servicios.InformacionEmpresaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service("informacionempresa")
public class InformacionEmpresaServicioImpl implements InformacionEmpresaServicio {

    @Autowired
    @Qualifier("clientemongoservicio")
    ClienteMongoServicioImpl clientemongoservicio;

    @Override
    public InformacionEmpresa obtenerDatos() {
        MongoOperations mongoops = clientemongoservicio.clienteMongo();
        return mongoops.find(new Query(), InformacionEmpresa.class).get(0);
    }

}
