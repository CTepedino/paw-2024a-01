import {Component, inject, OnInit} from '@angular/core';
import {MatFormField, MatHint, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatOption} from "@angular/material/core";
import {MatSelect} from "@angular/material/select";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {catchError, map, throwError} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";
import {EditBookService} from "./store/edit-book.service";
import {Title} from "@angular/platform-browser";
import {FileInputComponent} from "../../../../shared/components/file-input/file-input.component";
import {ActionButtonComponent} from "../../../../shared/components/action-button/action-button.component";
import {ContentCardComponent} from "../../../../shared/components/content-card/content-card.component";
import {BookGenre} from "../../../../shared/model/book/bookGenre";
import {fileTypeValidator} from "../../../../shared/validators/fileTypeValidator";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-edit-book',
	imports: [
		ActionButtonComponent,
		ContentCardComponent,
		FileInputComponent,
		MatFormField,
		MatHint,
		MatInput,
		MatLabel,
		MatOption,
		MatSelect,
		ReactiveFormsModule,
		FileInputComponent,
		ActionButtonComponent
	],
  templateUrl: './edit-book.component.html',
  styleUrl: './edit-book.component.scss'
})
export class EditBookComponent implements OnInit{
	title = inject(Title);
	editBookService = inject(EditBookService);
	router = inject(Router);
	route = inject(ActivatedRoute);

	bookId: any;

	protected readonly BookGenre = BookGenre;
	protected readonly Object = Object;

	form: FormGroup;

	constructor(private fb: FormBuilder, private translate: TranslateService) {
		this.translate.get('EDIT_BOOK_BROWSER').subscribe(translatedTitle => {
			this.title.setTitle(translatedTitle);
		});

		this.form = this.fb.group({
			title: ['', [Validators.required, Validators.maxLength(50)]],
			description: ['', [Validators.required, Validators.maxLength(1000)]],
			genre: [null, [Validators.required]],
			suggestedAge: [null, [Validators.required, Validators.max(100), Validators.min(0)]],
			price: [null, [Validators.required, Validators.max(100000000), Validators.min(0.1)]],
			pageCount: [null, [Validators.required, Validators.max(1000000), Validators.min(0)]],
			cover: this.fb.group({
				fileName: [''],
				fileData: [null, [fileTypeValidator(['image/*'])]],
			}),
			preview: this.fb.group({
				fileName: [''],
				fileData: [null, [fileTypeValidator(['application/pdf'])]],
			}),
			file: this.fb.group({
				fileName: [''],
				fileData: [null, [fileTypeValidator(['application/pdf'])]],
			}),
		});
	}

	ngOnInit() {
		this.bookId = this.route.snapshot.paramMap.get('id');
		this.editBookService.getBook(this.bookId).pipe(map((book) => {
			this.form.patchValue({
				title: book.title,
				description: book.description,
				genre: book.genre,
				suggestedAge: book.suggestedAge,
				price: book.price,
				pageCount: book.pageCount,
			});
		})).subscribe();
	}

	getCoverFormGroup(): FormGroup{
		return this.form.get('cover') as FormGroup;
	}

	getPreviewFormGroup(): FormGroup{
		return this.form.get('preview') as FormGroup;
	}

	getFileFormGroup(): FormGroup{
		return this.form.get('file') as FormGroup;
	}

	onSubmit() {
		if (this.form.valid){

			this.editBookService.edit(this.form).pipe(
				map(() => {
					this.router.navigate([`/book`, this.bookId]);
				}), catchError((err) => {
					console.log(err);
					return throwError(() => 'edit failed');
				})
			).subscribe();
		}
	}

}
