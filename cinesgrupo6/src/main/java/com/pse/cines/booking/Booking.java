/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pse.cines.booking;

import com.pse.cines.entities.Movie;
import com.pse.cines.entities.ShowTiming;
import static com.pse.cines.entities.ShowTiming_.movieId;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import javax.faces.flow.FlowScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Isabel
 */
@Named
@FlowScoped("booking")
public class Booking implements Serializable{
    int movieId;
    
    @PersistenceContext
    EntityManager em;
    
    String startTime;
    int startTimeId;
    
    private double precio = new Double(0);
    private String tarjeta = "";
    private Date fecha = new Date(); 

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movield) {
        this.movieId = movield;
    }
    
    public String getMovieName() {
        try {
            return em.createNamedQuery("Movie.findById", Movie.class).setParameter("id", movieId).getSingleResult().getName();
        } catch (NoResultException e) {
            return "";
        }
    }
    
    public String getStartTime() {
        return startTime;
    }
    
    public void setStartTime(String startTime) {
        StringTokenizer tokens = new StringTokenizer(startTime, ",");
        startTimeId = Integer.parseInt(tokens.nextToken());
        this.startTime = tokens.nextToken();
    }
    
    public int getStartTimeId() {
        return startTimeId;
    } 
    
    public String getTheater() {
        try {
            List<ShowTiming> list = em.createNamedQuery("ShowTiming.findByMovieAndTimingId", ShowTiming.class).setParameter("movieId", movieId).setParameter("timingId", startTimeId).getResultList();
            if (list.isEmpty()) {
                return "none";
            }
            return list.get(0).getTheaterId().getId().toString();
            
         } catch (NoResultException e) {
         return "none";
        }
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    
    
}
