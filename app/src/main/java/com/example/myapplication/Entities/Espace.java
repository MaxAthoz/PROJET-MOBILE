package com.example.myapplication.Entities;

import java.io.Serializable;

public class Espace implements Serializable {

    private Integer id;
    private String nomEspace;
    private Integer idUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomEspace() {
        return nomEspace;
    }

    public void setNomEspace(String nomEspace) {
        this.nomEspace = nomEspace;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return "Espace{" +
                "id=" + id +
                ", nomEspace='" + nomEspace + '\'' +
                ", idUser=" + idUser +
                '}';
    }
}
