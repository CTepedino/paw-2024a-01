import {booleanAttribute, Component, input} from '@angular/core';
import {MatFabButton} from "@angular/material/button";

@Component({
  selector: 'app-delete-button',
  imports: [
    MatFabButton
  ],
  templateUrl: './delete-button.component.html',
  styleUrl: './delete-button.component.scss'
})
export class DeleteButtonComponent {
  submit = input(false, {transform: booleanAttribute});
  disabled = input(false, {transform: booleanAttribute});
}
