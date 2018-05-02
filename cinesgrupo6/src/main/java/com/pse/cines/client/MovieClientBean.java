/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pse.cines.client;

import com.pse.cine.json.MovieReader;
import com.pse.cine.json.MovieWriter;
import com.pse.cines.entities.Movie;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;


@Named 
@RequestScoped
public class MovieClientBean {
    
    Client client;
    WebTarget target;
    @Inject MovieBackingBean bean;

    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();
        target = client.target("http://localhost:8080/cinesgrupo6/webresources/com.pse.cines.entities.movie");
    }

    @PreDestroy
    public void destroy() {
        client.close();
    }
    
    
    public Movie[] getMovies() {
        return target.request().get(Movie[].class);
    }
    
    
    public Movie getMovie() {
        /*Movie m = target
                .path("{movieId}")
                .resolveTemplate("movieId", bean.getMovieId()).request()
                .get(Movie.class);
        return m;*/
       
        return target
                .register(MovieReader.class)
                .path("{movieId}")
                .resolveTemplate("movieId", bean.getMovieId()) .request(MediaType.APPLICATION_JSON) .get(Movie.class);
    }
    
    public void deleteMovie() {
        target.path("{movieId}")
                .resolveTemplate("movieId", bean.getMovieId()).request()
                .delete();
    }
    
    public void addMovie() {
        Movie m = new Movie();
        m.setId(1);
        m.setName(bean.getMovieName());
        m.setActors(bean.getActors());
        target.register(MovieWriter.class)
                .request()
                .post(Entity.entity(m, MediaType.APPLICATION_JSON));
    }

  
    
}
