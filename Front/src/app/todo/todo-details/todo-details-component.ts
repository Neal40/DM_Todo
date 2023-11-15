import {Component, Input} from '@angular/core';
import {Todo} from "../../model/Todo";
import {ActivatedRoute, Router} from "@angular/router";
import {TodoService} from "../../services/todo.service";

@Component({
  selector: 'app-todo-details',
  templateUrl: './todo-details-component.html',
  styleUrls: ['./todo-details-component.css']
})
export class TodoDetailsComponent {

  @Input() todo!: Todo
  mode: string = "Display";

  constructor(
    private todoService: TodoService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  changeMode() {
    this.mode = (this.mode === "Edit") ? "Display" : "Edit";
  }

  updateTodo() {
    console.log("update")
    this.todoService.update(this.todo).subscribe(() => this.changeMode());
  }

  deleteTodo() {
    console.log("delete");
    if (confirm("Voulez-vous vraiment supprimer cette tâche?")) {
      this.todoService.delete(this.todo).subscribe(() => {
        this.router.navigate(['/todos']); // Redirection vers la liste des todos
     });
   }
  }
}
