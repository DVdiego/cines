/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pse.cines.jaas;

import com.pse.cines.entities.Users;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;


/**
 *
 * @author home
 */
@ManagedBean
@SessionScoped
public class LoginView implements Serializable{
    
    private String email;
    private String password;     
    private static Logger log = Logger.getLogger(RegisterView.class.getName());     
    @Inject
    private UserEJB userEJB;   
    private Users user;
    
    public Users getAuthenticatedUser() {
        return user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static Logger getLog() {
        return log;
    }

    public static void setLog(Logger log) {
        LoginView.log = log;
    }

    public UserEJB getUserEJB() {
        return userEJB;
    }

    public void setUserEJB(UserEJB userEJB) {
        this.userEJB = userEJB;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
    
    
    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.login(email, password);
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Login incorrecto!", null));
            return "login";
        }
        this.user = userEJB.findByEmail(request.getUserPrincipal().getName());
        
        if (request.isUserInRole("users")) {
            return "/users/privatepage?faces-redirect=true";
        } else if (request.isUserInRole("admin")) {
            return "/admin/privatepage?faces-redirect=true";
        } else {
            return "login";
        }
    }
    
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            this.user = null;
            request.logout();
            ((HttpSession) context.getExternalContext().getSession(false)).invalidate();
        } catch (ServletException e) {
            log.log(Level.SEVERE, "Fallo durante el proceso de logout!", e);
        }
        return "/index?faces-redirect=true";
    }
    
}
