package DM_Todo.Todo.todo;

import DM_Todo.Todo.exceptions.ExceptionHandlingAdvice;
import com.fasterxml.jackson.databind.ObjectMapper;
import DM_Todo.Todo.exceptions.ResourceAlreadyExistsException;
import DM_Todo.Todo.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@ContextConfiguration(classes = TodoController.class)
@Import(ExceptionHandlingAdvice.class)
public class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean(name = "jpa")
    TodoService todoService;

    private List<Todo> todos;

    @BeforeEach
    void setUp() {
        todos = new ArrayList<>() {{
            add(new Todo(1L, "Faire les courses", "2023-11-15", "Pas fait"));
            add(new Todo(2L, "Réviser pour l'examen", "2023-11-17", "Pas fait"));
            add(new Todo(3L, "Rendre le devoir", "2023-11-20", "Fait"));
        }};
        when(todoService.getAll()).thenReturn(todos);
        when(todoService.getById(2L)).thenReturn(todos.get(1));
        when(todoService.getById(49L)).thenThrow(ResourceNotFoundException.class);
    }
    // Réponse correcte : code d'état 200, avec la liste des tâches.
    @Test
    void whenGettingAll_shouldGet3_andBe200() throws Exception {
        mockMvc.perform(get("/todos")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$", hasSize(3))
        ).andDo(print());
    }
    // Réponse correcte : code d'état 200, avec les détails de la tâche avec l'ID 2.
    @Test
    void whenGettingId2L_shouldReturnSame() throws Exception {
        mockMvc.perform(get("/todos/2")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$.id", is(2))
        ).andExpect(jsonPath("$.contenu", is("Réviser pour l'examen"))
        ).andExpect(jsonPath("$.dateCreation", is("2023-11-17"))
        ).andExpect(jsonPath("$.statut", is("Tâche non accomplie"))
        ).andReturn();
    }
    // Réponse correcte : code d'état 404, car la tâche avec l'ID 49 n'existe pas.
    @Test
    void whenGettingUnexistingId_should404() throws Exception {
        mockMvc.perform(get("/todos/49")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()
        ).andDo(print());
    }

    // Enlever le CrossOrigin dans TodoController sinon erreur 403 à cause du CORS request
    // Une fois le test réalisé, il faut le remettre
    // Réponse correcte qaund je l'enlève : code d'état 201, avec l'en-tête « Location » indiquant l'URL de la ressource nouvellement créée.
    @Test
    void whenCreatingNew_shouldReturnLink_andShouldBeStatusCreated() throws Exception {
        Todo newTodo = new Todo(89L, "Nouvelle tâche", "2024-11-15", "Pas faite");
        ArgumentCaptor<Todo> todoReceived = ArgumentCaptor.forClass(Todo.class);
        when(todoService.create(any())).thenReturn(newTodo);

        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON_UTF8) // Set the content type
                .content(new ObjectMapper().writeValueAsString(newTodo))
        ).andExpect(status().isCreated()
        ).andExpect(header().string("Location", "/todos/" + newTodo.getId())
        ).andDo(print());

        verify(todoService).create(todoReceived.capture());
        assertEquals(newTodo, todoReceived.getValue());
    }

    // Réponse correcte : code d'état 409 (conflit), car il tente de créer une ressource avec un ID existant.
    @Test
    void whenCreatingWithExistingId_should409() throws Exception {
        when(todoService.create(any())).thenThrow(ResourceAlreadyExistsException.class);
        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(this.todos.get(2)))
        ).andExpect(status().isConflict()
        ).andDo(print());
    }

    // Réponse correcte : code d'état 204 (aucun contenu), car il met à jour la ressource existante.
    @Test
    void whenUpdating_shouldReceiveTodoToUpdate_andReturnNoContent() throws Exception {
        Todo initialTodo = todos.get(1);
        Todo updatedTodo = new Todo(initialTodo.getId(), "Réviser pour l'examen (mis à jour)", "2023-11-18", "Pas fait");
        ArgumentCaptor<Todo> todoReceived = ArgumentCaptor.forClass(Todo.class);

        mockMvc.perform(put("/todos/" + initialTodo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedTodo))
        ).andExpect(status().isNoContent());

        verify(todoService).update(anyLong(), todoReceived.capture());
        assertEquals(updatedTodo, todoReceived.getValue());
    }

   //  Réponse correcte : code d'état 204
    @Test
    void whenDeletingExisting_shouldCallServiceWithCorrectId_andSendNoContent() throws Exception {
        Long id = 3L;

        mockMvc.perform(delete("/todos/" + id)
        ).andExpect(status().isNoContent()
        ).andDo(print());

        ArgumentCaptor<Long> idReceived = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(todoService).delete(idReceived.capture());
        assertEquals(id, idReceived.getValue());
    }
}
