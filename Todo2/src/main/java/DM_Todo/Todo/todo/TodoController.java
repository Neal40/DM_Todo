package DM_Todo.Todo.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("todos")
@CrossOrigin(origins = "http://localhost:4200")
public class TodoController {
    private final TodoService todoService;

    @Autowired
    public TodoController(@Qualifier("jpa") TodoService todoService) {
        this.todoService = todoService;
    }


    @GetMapping("")
    public List<Todo> getAll() {
        return todoService.getAll();
    }

    @GetMapping("/{id}")
    public Todo getById(@PathVariable long id) {
        return todoService.getById(id);
    }

    @PostMapping("")
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        Todo created_todo = todoService.create(todo);
        return ResponseEntity.created(URI.create("/todos/"+ created_todo.getId())).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTodo(@PathVariable long id, @RequestBody Todo todo) {
        todoService.update(id, todo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable long id) {
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
