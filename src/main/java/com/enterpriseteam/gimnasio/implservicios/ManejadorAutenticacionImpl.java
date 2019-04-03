/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enterpriseteam.gimnasio.implservicios;

import com.enterpriseteam.gimnasio.entidades.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

/**
 *
 * @author Angelho
 */
@Service("manejadordeautenticacion")
public class ManejadorAutenticacionImpl implements AuthenticationSuccessHandler {

    @Autowired
    @Qualifier("autenticacionservicio")
    AutenticacionServicioImpl autenticacionservicio;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication autenticacion) throws IOException, ServletException {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        request.getSession().setAttribute("login", usuario.getLogin());
        /*request.getSession().setAttribute("permisos", usuario.getPermisos());
        request.getSession().setAttribute("usuario", usuario.getCorreo());
        request.getSession().setAttribute("nombres", usuario.getNombres());
        request.getSession().setAttribute("apellidos", usuario.getApellidos());
        request.getSession().setAttribute("dni", usuario.getDni());
        //request.getSession().setAttribute("coachs", usuarioservicio.listarCoach());
        request.getSession().setAttribute("roles", usuario.getRol());
        request.getSession().setAttribute("stockactivo", usuario.isStockactivo());*/
        response.sendRedirect(request.getContextPath() + "/principal");
    }

}
