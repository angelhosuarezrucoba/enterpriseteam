package com.enterpriseteam.gimnasio.controladores;

import com.enterpriseteam.gimnasio.entidades.Usuario;
import com.enterpriseteam.gimnasio.servicios.InformacionEmpresaServicio;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Clase ControladorLogin Maneja el control de acceso de los usuarios.
 *
 * @author Angelho
 */
@Controller
@RequestMapping("")
public class ControladorLogin {

    @Autowired
    @Qualifier("informacionempresa")
    InformacionEmpresaServicio informacionempresa;

//    @Autowired
//    @Qualifier("dashboardservicio")
//    DashboardServicio dashboardservicio;
//
//    @Autowired
//    @Qualifier("validaciondeusuario")
//    ValidacionDeUsuario validaciondeusuario;
//
//    @Autowired
//    @Qualifier("loginservicio")
//    LoginServicio loginservicio;
//
//    @Autowired
//    @Qualifier("stockservicio")
//    StockServicio stockservicio;
//    public static final String CONTROLADOR_LOGIN = "/login";
//    public static final String VISTA_LOGIN = "login";
//    public static final String CONTROLADOR_PRINCIPAL = "/principal";
//    public static final String VISTA_PRINCIPAL = "principal";
    @GetMapping("/login")
    public ModelAndView Login(@RequestParam(name = "error", required = false) String error, @RequestParam(name = "logout", required = false) String logout, HttpSession session) {
        ModelAndView modelologin = new ModelAndView("login");
        if (session.getAttribute("login") != null) {
            modelologin.setViewName("redirect:/principal");
        } else {            
            modelologin.addObject("error", error);
            modelologin.addObject("logout", logout);
            modelologin.addObject("usuario", new Usuario());
            modelologin.addObject("informacionempresa", informacionempresa.obtenerDatos());
        }
        return modelologin;
    }

    @GetMapping({"/principal", "/"})
    public ModelAndView principal(HttpSession session) {
        ModelAndView modeloprincipal = new ModelAndView("principal");
//
//        boolean esentrenador = ((((List<String>) session.getAttribute("roles")).contains("operador")
//                || ((List<String>) session.getAttribute("roles")).contains("coach")) && (boolean) session.getAttribute("stockactivo"));
//
//        if (esentrenador) {
//            modeloprincipal.addObject("stock", stockservicio.traerStock(session));
//        }
//        modeloprincipal.addObject("esentrenador", esentrenador);
//        modeloprincipal.addObject("totalafiliados", dashboardservicio.totalafiliados(session));
//        modeloprincipal.addObject("nombres", session.getAttribute("nombres"));
//        modeloprincipal.addObject("permisos", session.getAttribute("permisos"));
        return modeloprincipal;
    }

//    @GetMapping("/usuario")
//    public ModelAndView usuario(HttpSession session) {
//        ModelAndView modelousuario = new ModelAndView("usuario");
//
//        boolean esentrenador = ((((List<String>) session.getAttribute("roles")).contains("operador")
//                || ((List<String>) session.getAttribute("roles")).contains("coach")) && (boolean) session.getAttribute("stockactivo"));
//
//        if (esentrenador) {
//            modelousuario.addObject("stock", stockservicio.traerStock(session));
//        }
//        modelousuario.addObject("esentrenador", esentrenador);
//        modelousuario.addObject("nombres", session.getAttribute("nombres"));
//        modelousuario.addObject("apellidos", session.getAttribute("apellidos"));
//        modelousuario.addObject("dni", session.getAttribute("dni"));
//        modelousuario.addObject("permisos", session.getAttribute("permisos"));
//        return modelousuario;
//    }
}
