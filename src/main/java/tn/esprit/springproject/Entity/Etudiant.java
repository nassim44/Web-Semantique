package tn.esprit.springproject.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity


@Table( name = "etudiant")
public class Etudiant implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "idEtudiant")
    private Long idEtudiant;
    private String nomEt;
    private String prenomET;
    private Long cin;
    private String ecole;
    private Date dateNaissance;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Reservation> reservations;

    // Getter pour idEtudiant
    public Long getIdEtudiant() {
        return idEtudiant;
    }

    // Setter pour idEtudiant
    public void setIdEtudiant(Long idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    // Getter pour nomEt
    public String getNomEt() {
        return nomEt;
    }

    // Setter pour nomEt
    public void setNomEt(String nomEt) {
        this.nomEt = nomEt;
    }

    // Getter pour prenomET
    public String getPrenomET() {
        return prenomET;
    }

    // Setter pour prenomET
    public void setPrenomET(String prenomET) {
        this.prenomET = prenomET;
    }

    // Getter pour cin
    public Long getCin() {
        return cin;
    }

    // Setter pour cin
    public void setCin(Long cin) {
        this.cin = cin;
    }

    // Getter pour ecole
    public String getEcole() {
        return ecole;
    }

    // Setter pour ecole
    public void setEcole(String ecole) {
        this.ecole = ecole;
    }

    // Getter pour dateNaissance
    public Date getDateNaissance() {
        return dateNaissance;
    }

    // Setter pour dateNaissance
    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    // Getter pour reservations
    public Set<Reservation> getReservations() {
        return reservations;
    }

    // Setter pour reservations
    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }
    @Override
    public String toString() {
        return "Etudiant{" +
                "idEtudiant=" + idEtudiant +
                ", nomEt='" + nomEt + '\'' +
                ", prenomET='" + prenomET + '\'' +
                ", cin=" + cin +
                ", ecole='" + ecole + '\'' +
                ", dateNaissance=" + dateNaissance +
                '}';
    }

}


