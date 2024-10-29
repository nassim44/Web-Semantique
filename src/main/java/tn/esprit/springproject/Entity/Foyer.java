package tn.esprit.springproject.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import tn.esprit.springproject.Entity.Bloc;
import tn.esprit.springproject.Entity.Universite;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table( name = "foyer")
public class Foyer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idFoyer")
    private Long idFoyer;
    @Column(name = "nomFoyer")
    private String nomFoyer;
    @Column(name = "capaciteFoyer")
    private Long capaciteFoyer;
    @OneToOne(mappedBy="foyer")
    private Universite universite;

    @OneToMany(mappedBy="foyer")
    private Set<Bloc> bloc;
}

