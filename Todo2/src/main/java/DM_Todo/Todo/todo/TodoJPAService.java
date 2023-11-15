package DM_Todo.Todo.todo;

import DM_Todo.Todo.exceptions.ResourceAlreadyExistsException;
import DM_Todo.Todo.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("jpa")
public class TodoJPAService implements TodoService {


    @Autowired
    private TodoRepository todoRepository;

    @Override
    public List<Todo> getAll() {
        return todoRepository.findAll();
    }

    @Override
    public Todo getById(long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", id));
    }

    @Override
    public Todo create(Todo newTodo) throws ResourceAlreadyExistsException {
        if (todoRepository.existsById(newTodo.getId())) {
            throw new ResourceAlreadyExistsException("Todo existe déjà !", newTodo);
        } else {
            return todoRepository.save(newTodo);
        }
    }

    @Override
    public void update(long id, Todo updatedTodo) throws ResourceNotFoundException {
        if (todoRepository.existsById(id)) {
            updatedTodo.setId(id);
            todoRepository.save(updatedTodo);
        } else {
            throw new ResourceNotFoundException("Todo pas trouvée !", id);
        }
    }

    @Override
    public void delete(long id) throws ResourceNotFoundException {
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Todo pas trouvée !", id);
        }
    }
}
