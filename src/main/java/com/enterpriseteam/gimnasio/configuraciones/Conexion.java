/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enterpriseteam.gimnasio.configuraciones;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class Conexion {
    
    @Bean 
    public MongoClient mongo(){
        return new MongoClient(new MongoClientURI("mongodb://admin:2740522ab@enterpriseteamfit-shard-00-00-3awbk.mongodb.net:27017,enterpriseteamfit-shard-00-01-3awbk.mongodb.net:27017,enterpriseteamfit-shard-00-02-3awbk.mongodb.net:27017/test?ssl=true&replicaSet=enterpriseteamfit-shard-0&authSource=admin&retryWrites=true"));
    }

    @Bean
    public MongoTemplate dameConexion() {         
        return new MongoTemplate(mongo(), "gimnasio");
    }
}
