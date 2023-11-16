package DM_Todo.Todo.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import DM_Todo.Todo.exceptions.ResourceAlreadyExistsException;
import DM_Todo.Todo.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes=TodoJPAService.class)
public class TodoServiceTest {
    @Autowired
    private TodoService todoService;

    @MockBean
    private TodoRepository todoRepository;
    private List<Todo> todos;

    @BeforeEach
    void setUp() {
        todos = new ArrayList<>(){{
            add(new Todo(1L, "Faire les courses", "2023-11-15", "Pas fait"));
            add(new Todo(2L, "Réviser pour l'examen", "2023-11-17", "Pas fait"));
            add(new Todo(3L, "Rendre le devoir", "2023-11-20", "Fait"));
        }};
        when(todoRepository.findAll()).thenReturn(todos);
    }

    @Test
    void whenGettingAll_shouldReturn3() {
        assertEquals(3, todoService.getAll().size());
    }

    @Test
    void whenGettingById_shouldReturnIfExists_andBeTheSame() {
        when(todoRepository.findById(1L)).thenReturn((Optional.of(todos.get(0))));
        when(todoRepository.findById(12L)).thenReturn((Optional.empty()));
        assertAll(
                () -> assertEquals(todos.get(0), todoService.getById(1L)),
                () -> assertThrows(ResourceNotFoundException.class, () -> todoService.getById(12L))
        );
    }

    @Test
    void whenCreating_ShouldReturnSame() {
        Todo toCreate = new Todo(5L, "Nouvelle tâche", "2024-11-15", "Pas faite");
        assertEquals(toCreate, todoService.create(toCreate));
    }

    @Test
    void whenCreatingWithSameId_shouldReturnEmpty() {
        Todo same_todo = todos.get(0);

        assertThrows(ResourceAlreadyExistsException.class, ()->todoService.create(same_todo));
    }

    @Test
    void whenUpdating_shouldModifyTodo() {
        Todo initialTodo = todos.get(2);
        Todo newTodo = new Todo(initialTodo.getId(), "Rendre le devoir (mis à jour)", "2023-11-21", "Fait");

        todoService.update(newTodo.getId(), newTodo);
        Todo updated_todo = todoService.getById(initialTodo.getId());
        assertEquals(newTodo, updated_todo);
        assertTrue(todoService.getAll().contains(newTodo));
    }

    @Test
    void whenUpdatingNonExisting_shouldThrowException() {
        Todo todo = todos.get(2);

        assertThrows(ResourceNotFoundException.class, () -> todoService.update(75L, todo));
    }

    @Test
    void whenDeletingExistingTodo_shouldNotBeInTodosAnymore() {
        Todo todo = todos.get(1);
        long id = todo.getId();

        todoService.delete(id);
        assertFalse(todoService.getAll().contains(todo));
    }

    @Test
    void whenDeletingNonExisting_shouldThrowException() {
        long id = 68L;

        assertThrows(ResourceNotFoundException.class, ()->todoService.delete(id));
    }

}