import {Component, inject} from "@angular/core";
import {CommonModule} from "@angular/common";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {ReactiveFormsModule} from "@angular/forms";
import {RouterModule} from "@angular/router";
import {RegisterConfirmationComponent} from "./components/register-confirmation/register-confirmation.component";
import {RegisterFormComponent} from "./components/register-form/register-form.component";
import {Title} from "@angular/platform-browser";

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    ReactiveFormsModule,
    RouterModule,
    RegisterConfirmationComponent,
    RegisterFormComponent
  ],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignupComponent {
  title = inject(Title);

  constructor() {
    this.title.setTitle('Sign Up')
  }

  registering = true;
  email = '';

  registered(email: string){
    this.email = email;
    this.registering = false;
  }
}