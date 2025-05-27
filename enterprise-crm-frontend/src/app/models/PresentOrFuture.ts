import { AbstractControl, ValidationErrors } from '@angular/forms';

export function futureDateValidator(control: AbstractControl): ValidationErrors | null {
    const selectedDate = new Date(control.value);
    const today = new Date();
    today.setHours(0, 0, 0, 0); // Set time to midnight for comparison

    // Check if the selected date is today or in the future
    return selectedDate >= today ? null : { pastDate: true };
}
