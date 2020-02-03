package intra.poleemploi.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table(name="appli")
public class Appli implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String appliName;
    private String idAppliKM;
    //@OneToMany (cascade = CascadeType.ALL, mappedBy = "appli", orphanRemoval = true)
    @OneToMany(mappedBy = "appli")
    private Collection<Content> contents = new ArrayList<>();

    /*@Override
    public String toString() {
        return "Appli{" +
                "id=" + id +
                ", appliName='" + appliName + '\'' +
                ", idAppliKM='" + idAppliKM + '\'' +
                '}';
    }*/
}
