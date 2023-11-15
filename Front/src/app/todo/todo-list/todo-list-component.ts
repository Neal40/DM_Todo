import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TodoService } from '../../services/todo.service';
import { Todo } from '../../model/Todo';

@Component({
  selector: 'app-todo-list',
  templateUrl: './todo-list-component.html',
  styleUrls: ['./todo-list-component.css'],
})
export class TodoListComponent {
  todos: Todo[] = [];
  todoForm: FormGroup;
  isCreateMode: boolean = false;

  constructor(
    private todoService: TodoService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.todoForm = this.fb.group({
      contenu: ['', Validators.required],
      dateCreation: ['', Validators.required],
      statut: ['', Validators.required],
    });
    this.todoService.getTodos().subscribe((todos) => (this.todos = todos));
  }

  async createTodo() {
    let lastId = this.getLastId();
    let newTodo: Todo = {
      id: lastId + 1,
      contenu: this.todoForm.value.contenu,
      dateCreation: this.todoForm.value.dateCreation,
      statut: this.todoForm.value.statut,
    };

    await this.todoService.create(newTodo);
    this.todoForm.reset();
    this.isCreateMode = false;
    this.todos.push(newTodo);
  }

  private getLastId(): number {
    return this.todos.length > 0
      ? Math.max(...this.todos.map((todo) => todo.id))
      : 0;
  }

  toggleCreateMode() {
    this.isCreateMode = !this.isCreateMode;
    if (!this.isCreateMode) {
      this.todoForm.reset(); //
    }
  }
}
