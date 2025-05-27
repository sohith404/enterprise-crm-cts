import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { Store } from '@ngrx/store';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../service/auth.service';
import { Employee } from '../../../models/Employee';
import { selectAuthState } from '../../../store/auth/auth.selector';

@Component({
    selector: 'app-login',
    standalone: false,

    templateUrl: './login.component.html',
    styleUrl: './login.component.scss'
})
export class LoginComponent {
    constructor(
        private readonly authService: AuthService,
        private readonly store: Store,
        private readonly router: Router
    ) {}

    form!: FormGroup;
    loading = false;
    showError = false;
    responseMessage?: string;

    onSubmit(employee: Employee) {
        this.loading = true;
        this.authService.login(employee.email, employee.password).subscribe({
            next: (response) => {
                this.loading = false;
                this.responseMessage = 'Login Success';
                this.router.navigate(['/']);
            },
            error: (error: HttpErrorResponse) => {
                this.loading = false;
                this.showError = true;
                if (error.error && typeof error.error === 'object' && 'message' in error.error) {
                    this.responseMessage = error.error.message; // Access the backend's message
                } else if (error.error && typeof error.error === 'string') {
                    this.responseMessage = error.error; // Handle potential string error messages
                } else {
                    this.responseMessage = `Login failed: ${error.status} ${error.statusText}`; // Default error message
                }
            }
        });
    }

    ngOnInit(): void {
        this.store.select(selectAuthState).subscribe((authState) => {
            if (authState.user) {
                this.router.navigate(['/']);
            }
        });

        this.form = new FormGroup({
            email: new FormControl('', [Validators.required, Validators.email, Validators.pattern(/^[a-zA-Z0-9._%+-]+@enterprise\.com$/)]),
            password: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(20), Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/)])
        });
    }

    get emailErrorMessage() {
        if (this.form.get('email')?.hasError('required')) {
            return 'Email is required';
        }
        if (this.form.get('email')?.hasError('pattern')) {
            return 'Email must be a enterprise email';
        }
        return '';
    }

    get passwordErrorMessage() {
        if (this.form.get('password')?.hasError('required')) {
            return 'Password is required';
        }
        if (this.form.get('password')?.hasError('pattern')) {
            return 'Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character';
        }
        return '';
    }
}
