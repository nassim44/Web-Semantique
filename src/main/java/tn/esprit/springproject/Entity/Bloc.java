package tn.esprit.springproject.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@Table( name = "bloc")
public class Bloc implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "idBloc")
        private Long idBloc;
        @Column(name = "nomBloc")
        private String nomBloc;
        @Column(name = "capaciteBloc")
        private Long capaciteBloc;

        @ManyToOne
        private Foyer foyer ;

        @OneToMany(mappedBy = "bloc" )
        private Set<Chambre> Chambre ;


}

