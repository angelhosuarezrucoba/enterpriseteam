package com.enterpriseteam.gimnasio.configuraciones;

import com.enterpriseteam.gimnasio.implservicios.ManejadorAutenticacionImpl;
import com.enterpriseteam.gimnasio.implservicios.AutenticacionServicioImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ConfiguracionesSeguridad extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("manejadordeautenticacion")
    ManejadorAutenticacionImpl manejadordeautenticacion;

    @Autowired
    @Qualifier("autenticacionservicio")
    AutenticacionServicioImpl autenticacionservicio;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder autenticacion) throws Exception {
        autenticacion.userDetailsService(autenticacionservicio);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //IMPORTANTE;
        //Codigo que permite el ingreso de tildes y letras raras(ñ)       
//        CharacterEncodingFilter filter = new CharacterEncodingFilter();
//        filter.setEncoding("UTF-8");
//        filter.setForceEncoding(true);
//        http.addFilterBefore(filter, CsrfFilter.class);
        // 
//        http.csrf().ignoringAntMatchers("/smsenviados");
//        http.csrf().ignoringAntMatchers("/apifechas/fechas");
//        http.csrf().ignoringAntMatchers("/apifechas");
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN") //Cualquier URL que comience con "/ admin /" se restringirá a los usuarios que tengan el rol "ROLE_ADMIN". Observará que, dado que estamos invocando el hasRole método, no es necesario que especifique el prefijo "ROLE_".                                    
                .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')") //Cualquier URL que comience con "/ db /" requiere que el usuario tenga tanto "ROLE_ADMIN" como "ROLE_DBA". Notarás que como estamos usando la hasRoleexpresión no necesitamos especificar el prefijo "ROLE_".
                .antMatchers("/assets/**", "/img/**","/audio/**","/css/**","/js/**").permitAll()
                .anyRequest().authenticated() //Cualquier URL que no haya sido emparejada solo requiere que el usuario sea autenticado
                .and()
                .formLogin()
                .usernameParameter("login")
                .passwordParameter("clave")
                .loginPage("/login") //La configuración actualizada especifica la ubicación de la página de inicio de sesión.
                .loginProcessingUrl("/ingreso") // este metodo solo es un trigger y se activa solo , no necesita un controlador.            
                .defaultSuccessUrl("/principal",true).successHandler(manejadordeautenticacion)
                .permitAll()
                .and()
                .logout() //Proporciona soporte de cierre de sesión. Esto se aplica automáticamente cuando se usa WebSecurityConfigurerAdapter.                                                         
                .logoutUrl("/salir") //La URL que desencadena el cierre de sesión (por defecto es /logout). Si la protección CSRF está habilitada (por defecto), entonces la solicitud también debe ser un POST.                    
                .logoutSuccessUrl("/login?logout").permitAll() //La URL a la que se redirigirá después del cierre de sesión. El valor por defecto es /login?logout.                                     
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");

    }

}
