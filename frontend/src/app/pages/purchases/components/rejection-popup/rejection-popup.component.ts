import {Component, Inject} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {
	MAT_DIALOG_DATA,
	MatDialogActions,
	MatDialogContent,
	MatDialogRef,
	MatDialogTitle
} from "@angular/material/dialog";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-rejection-popup',
	imports: [
		MatButton,
		MatDialogActions,
		MatDialogContent,
		MatDialogTitle,
		TranslateModule
	],
  templateUrl: './rejection-popup.component.html',
  styleUrl: './rejection-popup.component.scss'
})
export class RejectionPopupComponent {
	constructor(
		public dialogRef: MatDialogRef<RejectionPopupComponent>,
		@Inject(MAT_DIALOG_DATA) public data: { reason: string }
	) {}

	closeDialog() {
		this.dialogRef.close();
	}
}
