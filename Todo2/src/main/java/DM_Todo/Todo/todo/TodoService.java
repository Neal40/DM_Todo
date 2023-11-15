package DM_Todo.Todo.todo;

import DM_Todo.Todo.exceptions.ResourceAlreadyExistsException;
import DM_Todo.Todo.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface TodoService {

    List<Todo> getAll();

    Todo getById(long id);

    Todo create(Todo newTodo) throws ResourceAlreadyExistsException;

    void update(long id, Todo updatedTodo) throws ResourceNotFoundException;

    void delete(long id) throws ResourceNotFoundException;
}
