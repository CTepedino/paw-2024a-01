import {booleanAttribute, Component, input} from '@angular/core';
import {MatFabButton} from "@angular/material/button";

@Component({
  selector: 'app-action-button',
	imports: [
		MatFabButton
	],
  templateUrl: './action-button.component.html',
  styleUrl: './action-button.component.scss'
})
export class ActionButtonComponent {
	submit = input(false, {transform: booleanAttribute});
	disabled = input(false, {transform: booleanAttribute});
}
