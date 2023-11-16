import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import {HttpClientModule} from "@angular/common/http";
import { TodoDetailsComponent } from './todo/todo-details/todo-details-component';
import { TodoListComponent } from './todo/todo-list/todo-list-component';
import { TodoBlockComponent } from './todo/todo-block/todo-block-component';
import { AppRoutingModule } from './app.routing.mdule';
import {RouterLink, RouterOutlet} from "@angular/router";
import {FormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    AppComponent,
    TodoBlockComponent,
    TodoDetailsComponent,
    TodoListComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    RouterOutlet,
    RouterLink,
    FormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
