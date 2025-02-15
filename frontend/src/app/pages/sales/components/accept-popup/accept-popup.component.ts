import {Component, output} from '@angular/core';
import {MatButtonModule} from "@angular/material/button";
import {FormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";
import {MatFormFieldModule} from "@angular/material/form-field";
import {
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";

@Component({
  selector: 'app-accept-popup',
  imports: [
      MatFormFieldModule,
      MatInputModule,
      FormsModule,
      MatButtonModule,
      MatDialogTitle,
      MatDialogContent,
      MatDialogActions,
      MatDialogClose],
  templateUrl: './accept-popup.component.html',
  styleUrl: './accept-popup.component.scss'
})
export class AcceptPopupComponent {
  constructor(public dialogRef: MatDialogRef<AcceptPopupComponent>) { }

  closeDialog() {
    this.dialogRef.close();
  }

  accept(){
      this.dialogRef.close({accept: true});
  }
}
