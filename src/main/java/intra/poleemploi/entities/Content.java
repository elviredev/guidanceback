package intra.poleemploi.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
@Table(name="content")
public class Content implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String idContentKM;
    private String contentName;
    private boolean published;
    private String typeService;
    private Integer nbAffichages;
    private Integer nbLectures;
    @ManyToOne
    @JoinColumn(name = "id_appli", nullable = false)
    private Appli appli;


}
