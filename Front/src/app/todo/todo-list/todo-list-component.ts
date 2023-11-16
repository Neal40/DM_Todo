import { Component } from '@angular/core';
import { TodoService } from '../../services/todo.service';
import { Todo } from '../../model/Todo';

@Component({
  selector: 'app-todo-list',
  templateUrl: './todo-list-component.html',
  styleUrls: ['./todo-list-component.css'],
})
export class TodoListComponent {
  todos: Todo[] = [];
  todo: Todo = {} as Todo;
  mode: string ="Create";

  constructor(
    private todoService: TodoService,
  ) {
    this.todoService.getTodos().subscribe((todos) => (this.todos = todos));
    }

  createTodo() {
    console.log("create")
    this.todoService.create(this.todo).subscribe(() => this.ModeCreate());
  }

  ModeCreate() {
  if (this.mode==="Create"){
    this.mode = "Annuler"
  }else {
      this.mode = "Create"
    }
  return this.mode;
  }
}

