package com.example.myapplication.Entities;

import java.util.Date;
import java.util.Objects;

public class Views {

    private Integer id;
    private String valeur;
    private Integer idEspace;
    private Integer idIndicateur;
    private Integer idUser;
    private Date date;
    private String nomIndicateur;
    private String type;
    private String nomEspace;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public Integer getIdEspace() {
        return idEspace;
    }

    public void setIdEspace(Integer idEspace) {
        this.idEspace = idEspace;
    }

    public Integer getIdIndicateur() {
        return idIndicateur;
    }

    public void setIdIndicateur(Integer idIndicateur) {
        this.idIndicateur = idIndicateur;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNomIndicateur() {
        return nomIndicateur;
    }

    public void setNomIndicateur(String nomIndicateur) {
        this.nomIndicateur = nomIndicateur;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNomEspace() {
        return nomEspace;
    }

    public void setNomEspace(String nomEspace) {
        this.nomEspace = nomEspace;
    }

    @Override
    public String toString() {
        return "Views{" +
                ", date=" + date +
                ", idEspace=" + idEspace +
                ", idIndicateur=" + idIndicateur +
                ", valeur='" + valeur + '\'' +
                ", idUser=" + idUser +
                ", nomIndicateur='" + nomIndicateur + '\'' +
                ", type='" + type + '\'' +
                ", nomEspace='" + nomEspace + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Views views = (Views) o;
        return Objects.equals(valeur, views.valeur) &&
                Objects.equals(idEspace, views.idEspace) &&
                Objects.equals(idIndicateur, views.idIndicateur) &&
                Objects.equals(idUser, views.idUser) &&
                Objects.equals(date, views.date) &&
                Objects.equals(nomIndicateur, views.nomIndicateur) &&
                Objects.equals(type, views.type) &&
                Objects.equals(nomEspace, views.nomEspace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valeur, idEspace, idIndicateur, idUser, date, nomIndicateur, type, nomEspace);
    }
}
