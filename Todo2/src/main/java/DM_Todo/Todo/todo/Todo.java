package DM_Todo.Todo.todo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Objects;

@Entity
public class Todo {

    @Id
    @NotNull()
    @Min(0)@Max(20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String contenu;
    private String datecreation;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return id == todo.id && Objects.equals(contenu, todo.contenu) && Objects.equals(datecreation, todo.datecreation) && Objects.equals(statut, todo.statut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contenu, datecreation, statut);
    }
}
