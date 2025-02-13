import {AbstractControl, ValidatorFn} from "@angular/forms";

export function fileTypeValidator(allowedTypes: string[]): ValidatorFn {
	return (control: AbstractControl) => {
		const file = control.value;

		if (!file) {
			return null;
		}

		if (file instanceof File) {
			const fileType = file.type;

			const isValid = allowedTypes.some((type) =>
				type === fileType || (type.endsWith('/*') && fileType.startsWith(type.split('/')[0]))
			);

			if (!isValid) {
				return { invalidFileType: true };
			}
		}

		return null;
	}
}