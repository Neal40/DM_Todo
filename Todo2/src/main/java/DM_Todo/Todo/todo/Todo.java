package DM_Todo.Todo.todo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Todo {

    @Id
    @NotNull()
    @Min(0)@Max(20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "contenu")
    private String contenu;
    @Column(name = "datecreation")
    private String datecreation;
    @Column(name = "statut")
    private String statut;

    public Todo() {
    }

    public Todo(long id, String contenu,String datecreation, String statut) {
        this.id = id;
        this.contenu = contenu;
        this.datecreation = datecreation;
        this.statut = statut;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getDateCreation() {
        return datecreation;
    }
    public void setDateCreation(String datecreation) {
        this.datecreation = datecreation;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
