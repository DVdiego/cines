/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pse.cines.jaas;

import com.pse.cines.entities.UserGroups;
import com.pse.cines.entities.Users;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author home
 */
@Stateless 
public class UserEJB {
    
    
    @PersistenceContext
    private EntityManager em;

    public Users createUser(Users user) {
        try {
            user.setPassword(AuthenticationUtils.encodeSHA256(user.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        UserGroups group = new UserGroups();
        group.setEmail(user.getEmail());
        group.setGroupname("users");
        em.persist(user);
        em.persist(group);

        return user;
    }
    
    
    public Users findByEmail(String email) {
        TypedQuery<Users> query = em.createNamedQuery("Users.findByEmail", Users.class);
        query.setParameter("email", email);
        Users user = null;
        try {
            user = query.getSingleResult();
        } catch (Exception e) {
        }
        return user;
    }
}
