/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enterpriseteam.gimnasio.implservicios;

import com.enterpriseteam.gimnasio.entidades.Usuario;
import com.enterpriseteam.gimnasio.servicios.ClienteMongoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Angelho
 */
@Service("autenticacionservicio")
public class AutenticacionServicioImpl implements UserDetailsService {

    @Autowired
    @Qualifier("clientemongoservicio")
    ClienteMongoServicio clientmongoservicio;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = null;
        MongoOperations mongoops = clientmongoservicio.clienteMongo();
        try {
            usuario = mongoops.findOne(new Query(Criteria.where("login").is(login)), Usuario.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (usuario == null) {
            throw new UsernameNotFoundException(login);
        } else {
            return usuario;
        }

    }

  /*  public List<String> listarCoach() {
        List<String> coachs = null;
        MongoOperations mongoops = clientmongoservicio.clienteMongo();
        try {

            Criteria criterios = new Criteria();
            criterios.andOperator(Criteria.where/("rol").in(Arrays.asList("coach", "operador")), Criteria.where("rol").ne("admin"));
            coachs = mongoops.find(new Query(criterios), Usuario.class).stream().map((t) -> t.getCorreo()).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coachs;
    }

    public Usuario traerUsuario(String dni) {
        Usuario usuario = null;
        MongoOperations mongoops = clientmongoservicio.clienteMongo();
        try {

            // usuario = mongoops.findOne(new Query(Criteria.where("rol").is("cliente").and("dni").is(dni)), Usuario.class);
            usuario = mongoops.findOne(new Query(Criteria.where("dni").is(dni)), Usuario.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public Usuario modificarMembresia(Usuario cliente, HttpSession session) {
        Usuario usuario = null;
        MongoOperations mongoops = clientmongoservicio.clienteMongo();
        try {
            usuario = mongoops.findAndModify(new Query(Criteria.where("dni").is(cliente.getDni())),
                    new Update().set("activo", cliente.isActivo()), FindAndModifyOptions.options().returnNew(true), Usuario.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public boolean modificarcliente(Usuario cliente, HttpSession session) {
        Usuario usuarioviejo = null;
        boolean modificarcliente = false;
        MongoOperations mongoops = clientmongoservicio.clienteMongo();
        try {
            usuarioviejo = mongoops.findOne(new Query(Criteria.where("dni").is(cliente.getDni())), Usuario.class);
            mongoops.updateFirst(new Query(Criteria.where("correo").is(session.getAttribute("usuario")).and("afiliados").
                    is(usuarioviejo.getCorreo())), new Update().set("afiliados.$", cliente.getCorreo()), Usuario.class);
            mongoops.updateFirst(new Query(Criteria.where("dni").is(cliente.getDni())),
                    new Update().set("nombres", cliente.getNombres()).
                            set("apellidos", cliente.getApellidos()).
                            set("correo", cliente.getCorreo()).
                            set("telefono", cliente.getTelefono()), Usuario.class);

            modificarcliente = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modificarcliente;
    }

    public boolean cambiarClave(Usuario usuario, HttpSession session) {
        Usuario cuenta = null;
        boolean cambiarclave = false;
        MongoOperations mongoops = clientmongoservicio.clienteMongo();
        try {
            cuenta = mongoops.findOne(new Query(Criteria.where("dni").is(usuario.getDni()).and("clave").is(usuario.getClavevieja())), Usuario.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cuenta != null) {

            mongoops.updateFirst(new Query(Criteria.where("dni").is(usuario.getDni()).and("clave").is(usuario.getClavevieja())),
                    new Update().set("clave", usuario.getClave()), Usuario.class);
            cambiarclave = true;
        }
        return cambiarclave;
    }

    public boolean membresiaGratis(String dni) {
        boolean membresiagratis = false;
        MongoOperations mongoops = clientmongoservicio.clienteMongo();
        try {
            mongoops.updateFirst(new Query(Criteria.where("dni").is(dni)),
                    new Update().set("membresiagratis", 2).set("activo", true), Usuario.class);
            membresiagratis = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return membresiagratis;
    }

    public boolean crearUsuario(Usuario usuario, HttpSession session) {
        boolean usuariocreado = false;
        FormatoDeFechas formato = new FormatoDeFechas();
        Usuario usuarioexistente;
        MongoOperations mongoops = clientmongoservicio.clienteMongo();
        try {
            usuarioexistente = mongoops.findOne(
                    new Query(new Criteria().orOperator(Criteria.where("dni").is(usuario.getDni()), Criteria.where("correo").is(usuario.getCorreo()))), Usuario.class);
        } catch (Exception e) {
            usuarioexistente = null;
        }
        try {
            if (usuarioexistente == null) {
                List<Permiso> listapermisos = mongoops.findAll(Permiso.class);
                usuario.setPermisos(listapermisos.stream()
                        .filter(permiso
                                -> usuario.getListapermisos().contains(permiso.getNombre())
                        ).collect(Collectors.toList()));
                usuario.setClave(usuario.getDni());
                usuario.setAfiliados(Arrays.asList());
                usuario.setActivo(true);
                usuario.setFechadeafiliacion(formato.convertirFechaString(new Date(), formato.FORMATO_FECHA));
                mongoops.insert(usuario);

                Stock stocklimpio = new Stock();
                stocklimpio.setBatido(0);
                stocklimpio.setDriver(0);
                stocklimpio.setProteina(0);
                stocklimpio.setTe(0);
                mongoops.insert(new StockUsuario(usuario.getDni(), usuario.getCorreo(), stocklimpio, Arrays.asList()));

                usuariocreado = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuariocreado;
    }

    public List<String> traerListaUsuarios() {

        List<String> listausuarios;

        MongoOperations mongoops = clientmongoservicio.clienteMongo();
        Query query = new Query();
        query.fields().include("correo");
        listausuarios = mongoops.find(query, Usuario.class).stream().map((t) -> {
            return t.getCorreo();
        }).collect(Collectors.toList());

        return listausuarios;
    }

    public Respuesta guardarDatos(Datos datos) {
        Respuesta respuesta = new Respuesta();
        MongoOperations mongoops = clientmongoservicio.clienteMongo();
        try {

            if (!datos.getReferido().equalsIgnoreCase(datos.getCorreo())) {

                mongoops.updateFirst(new Query(Criteria.where("afiliados").is(datos.getCorreo())), new Update().pull("afiliados", datos.getCorreo()), Usuario.class);
                mongoops.updateFirst(new Query(Criteria.where("correo").is(datos.getCorreo())),
                        new Update()
                                .set("nombres", datos.getNombres())
                                .set("telefono", datos.getTelefono())
                                .set("clave", datos.getClave())
                                .set("dni", datos.getDni())
                                .set("rol", datos.getRol())
                                .set("apellidos", datos.getApellidos())
                                .set("sexo", datos.getSexo())
                                .set("referido", datos.getReferido())
                                .set("fechadeafiliacion", datos.getFechadeafiliacion())
                                .set("mailusuario", datos.getMailusuario())
                                .set("activo", datos.isActivo())
                                .set("sesion", datos.getSesion())
                                .set("clase", datos.getClase())
                                .set("tipocoach", datos.getTipocoach())
                                .set("stockactivo", datos.isStockactivo()),
                        Datos.class);

                mongoops.updateFirst(new Query(Criteria.where("correo").is(datos.getReferido())), new Update().push("afiliados", datos.getCorreo()), Usuario.class);
                respuesta.setEstado(true);
                respuesta.setMensaje("Se actualizaron los datos correctamente");
            } else {
                respuesta.setEstado(false);
                respuesta.setMensaje("El referido no puede ser el mismo usuario");
            }

        } catch (Exception e) {
            respuesta.setEstado(false);
            respuesta.setMensaje("No Se actualizaron los datos correctamente");
        }
        return respuesta;
    }

    public Respuesta guardarPermisos(Permisos permisos) {
        Respuesta respuesta = new Respuesta();
        MongoOperations mongoops = clientmongoservicio.clienteMongo();
        try {
            List<Permiso> listapermisos = mongoops.find(new Query(Criteria.where("nombre").in(permisos.getListapermisos())), Permiso.class);
            mongoops.updateFirst(new Query(Criteria.where("correo").is(permisos.getCorreo())), new Update().set("permisos", listapermisos), Usuario.class);
            respuesta.setEstado(true);
            respuesta.setMensaje("Se actualizaron los permisos correctamente");
        } catch (Exception e) {
            respuesta.setEstado(false);
            respuesta.setMensaje("No Se actualizaron los permisos correctamente");
        }
        return respuesta;
    }

    public Respuesta guardarAfiliados(Afiliados afiliados) {
        Respuesta respuesta = new Respuesta();
        MongoOperations mongoops = clientmongoservicio.clienteMongo();
        try {
            if (!afiliados.getListaafiliados().contains(afiliados.getCorreo())) {

                Query query = new Query(Criteria.where("correo").is(afiliados.getCorreo()));
                query.fields().include("afiliados");
                List<String> afiliadosoriginales = mongoops.findOne(query, Usuario.class).getAfiliados();
                List<String> copialistado = afiliados.getListaafiliados();
                List<String> libres = new ArrayList<>();

                afiliadosoriginales.forEach((original) -> {
                    if (afiliados.getListaafiliados().contains(original)) {
                        copialistado.remove(original); // aqui el arreglo de nuevos se reduce
                    } else {
                        libres.add(original);
                    }
                    System.out.println("entre a afiliados originales");
                });
                System.out.println("pase 1");

                if (libres.size() > 0) {
                    mongoops.updateMulti(new Query(Criteria.where("correo").in(libres)),
                            new Update().set("referido", "libre"), Usuario.class);
                    mongoops.updateFirst(new Query(Criteria.where("correo").is("libre")), new Update().push("afiliados").each(libres), Usuario.class);
                }
                System.out.println("pase 2");
                System.out.println(copialistado.size());
                copialistado.forEach((nuevo) -> {
                    System.out.println("entre 2");
                    mongoops.updateFirst(new Query(Criteria.where("afiliados").is(nuevo)), new Update().pull("afiliados", nuevo), Usuario.class);
                });
                System.out.println("pase 3");
                mongoops.updateMulti(new Query(Criteria.where("correo").in(afiliados.getListaafiliados())),
                        new Update().set("referido", afiliados.getCorreo()), Usuario.class);
                System.out.println("pase 4");
                mongoops.updateFirst(new Query(Criteria.where("correo").is(afiliados.getCorreo())), new Update().set("afiliados", afiliados.getListaafiliados()), Usuario.class);

                respuesta.setEstado(true);
                respuesta.setMensaje("Se actualizaron los afiliados correctamente");
            } else {
                respuesta.setEstado(false);
                respuesta.setMensaje("No se puede afiliar el usuario a sí mismo");
            }  
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.setEstado(false);
            respuesta.setMensaje("No Se actualizaron los afiliados correctamente");
        }
        return respuesta;
    }
*/
}
