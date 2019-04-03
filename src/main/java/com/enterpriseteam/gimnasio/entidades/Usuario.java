/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enterpriseteam.gimnasio.entidades;

import java.util.Collection;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Asus
 */
@Document(collection = "usuario")
public class Usuario implements UserDetails {

    @Id
    private String id;
    private String login = "";
    private String clave = "";
    private List<GrantedAuthority> permisosotorgados;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getPermisosotorgados();
    }

    @Override
    public String getPassword() {
        return getClave();
    }

    @Override
    public String getUsername() {
        return getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public List<GrantedAuthority> getPermisosotorgados() {
        return permisosotorgados;
    }

    public void setPermisosotorgados(List<GrantedAuthority> permisosotorgados) {
        this.permisosotorgados = permisosotorgados;
    }

}
