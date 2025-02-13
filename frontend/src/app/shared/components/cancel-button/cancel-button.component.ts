import {booleanAttribute, Component, input} from '@angular/core';
import {MatFabButton} from "@angular/material/button";

@Component({
  selector: 'app-cancel-button',
	imports: [
		MatFabButton
	],
  templateUrl: './cancel-button.component.html',
  styleUrl: './cancel-button.component.scss'
})
export class CancelButtonComponent {
	submit = input(false, {transform: booleanAttribute});
	disabled = input(false, {transform: booleanAttribute});
}
