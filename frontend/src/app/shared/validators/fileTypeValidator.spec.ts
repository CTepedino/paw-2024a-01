import { AbstractControl } from '@angular/forms';
import {fileTypeValidator} from "./fileTypeValidator";

describe('fileTypeValidator', () => {

	it('should pass for a valid file type', () => {
		const allowedTypes = ['image/jpeg', 'image/png'];
		const control: AbstractControl = { value: new File([], 'test.jpg', { type: 'image/jpeg' }) } as AbstractControl;

		const result = fileTypeValidator(allowedTypes)(control);

		expect(result).toBeNull();
	});

	it('should error for an invalid file type', () => {
		const allowedTypes = ['image/jpeg', 'image/png'];
		const control: AbstractControl = { value: new File([], 'test.pdf', { type: 'application/pdf' }) } as AbstractControl;

		const result = fileTypeValidator(allowedTypes)(control);

		expect(result).toEqual({ invalidFileType: true });
	});

	it('should pass for no file provided', () => {
		const allowedTypes = ['image/jpeg', 'image/png'];
		const control: AbstractControl = { value: null } as AbstractControl;

		const result = fileTypeValidator(allowedTypes)(control);

		expect(result).toBeNull();
	});

	it('should pass for a file type with wildcard', () => {
		const allowedTypes = ['image/*'];
		const control: AbstractControl = { value: new File([], 'test.jpg', { type: 'image/jpeg' }) } as AbstractControl;

		const result = fileTypeValidator(allowedTypes)(control);

		expect(result).toBeNull();
	});

	it('should error for a file type with wildcard that doesnt match', () => {
		const allowedTypes = ['image/*'];
		const control: AbstractControl = { value: new File([], 'test.pdf', { type: 'application/pdf' }) } as AbstractControl;

		const result = fileTypeValidator(allowedTypes)(control);

		expect(result).toEqual({ invalidFileType: true });
	});

});
