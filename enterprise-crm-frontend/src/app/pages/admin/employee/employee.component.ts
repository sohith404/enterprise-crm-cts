import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ValidationErrors, Validators } from '@angular/forms';

import { Router } from '@angular/router';
import { AuthService } from '../../auth/service/auth.service';
import { Employee } from '../../../models/Employee';

@Component({
    selector: 'app-employee',
    standalone: false,

    templateUrl: './employee.component.html',
    styleUrl: './employee.component.scss'
})
export class EmployeeComponent implements OnInit {
    form!: FormGroup;
    userRegistered = false;
    loading = false;

    constructor(
        private readonly authService: AuthService,
        private readonly router: Router
    ) {}

    onSubmit(employee: Employee) {
        this.loading = true;
        this.authService.registerUser(employee).subscribe({
            next: () => {
                this.userRegistered = true;
                this.loading = false;
                this.router.navigate(['/pages/admin']);
            },
            error: () => {
                this.loading = false;
                this.router.navigate(['/auth/error']);
            }
        });
    }

    onCancel() {
        this.router.navigate(['/pages/admin']);
    }

    ngOnInit(): void {
        this.form = new FormGroup({
            email: new FormControl('', [Validators.required, Validators.email, Validators.pattern(/^[a-zA-Z0-9._%+-]+@enterprise\.com$/)]),
            name: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(20), Validators.pattern(/^[a-zA-Z ]+$/)]),
            password: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(20), Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/)]),
            confirmPassword: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(20), Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/), this.passwordMatchValidator()]),
            role: new FormControl('', [Validators.required]),
            img: new FormControl('')
        });
    }

    options = [
        { label: 'Manager', value: 'MANAGER' },
        { label: 'Employee', value: 'EMPLOYEE' }
    ];

    private passwordMatchValidator() {
        return (control: FormControl): ValidationErrors | null => {
            const password = this.form?.get('password')?.value;
            const confirmPassword = control.value;
            return password && confirmPassword && password !== confirmPassword ? { valid: false } : null;
        };
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
    get nameErrorMessage() {
        if (this.form.get('name')?.hasError('required')) {
            return 'Name is required';
        }
        if (this.form.get('name')?.hasError('pattern')) {
            return 'Name must contain only letters and spaces';
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
        if (this.form.get('password')?.value !== this.form.get('confirmPassword')?.value) {
            return 'Passwords are not matching.';
        }
        return '';
    }
    get confirmPasswordErrorMessage() {
        if (this.form.get('confirmPassword')?.hasError('required')) {
            return 'Confirm Password is required';
        }
        if (this.form.get('confirmPassword')?.hasError('pattern')) {
            return 'Confirm Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character';
        }
        if (this.form.get('password')?.value !== this.form.get('confirmPassword')?.value) {
            return 'Passwords are not matching.';
        }
        return '';
    }
    get roleErrorMessage() {
        if (this.form.get('role')?.hasError('required')) {
            return 'Role is required';
        }
        return '';
    }
}
