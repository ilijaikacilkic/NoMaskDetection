import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { MessagesComponent } from './messages/messages.component'
import { NgModule } from '@angular/core';


const routes: Routes = [
  { path: 'login', component: LoginComponent }, 
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'mainPage', component: MessagesComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
