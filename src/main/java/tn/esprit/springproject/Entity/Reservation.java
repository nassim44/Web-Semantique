package tn.esprit.springproject.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

@Entity
@Getter
@Setter
@Table( name = "Reservation")
public class Reservation implements Serializable {
    @Id
    @Column(name = "idReservation")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String idReservation;

    private String numReservation;

    @Temporal(TemporalType.DATE)
    LocalDate debutAnneeUniv;

    @Temporal(TemporalType.DATE)
    LocalDate finAnneeUniv;

     boolean estValide;

    @ManyToMany
    private Set<Etudiant> etudiants;
    }

