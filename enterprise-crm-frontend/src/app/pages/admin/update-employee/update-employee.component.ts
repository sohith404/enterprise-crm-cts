import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../auth/service/auth.service';
import { Store } from '@ngrx/store';
import { selectAuthState } from '../../../store/auth/auth.selector';

@Component({
    selector: 'app-update-employee',
    standalone: false,

    templateUrl: './update-employee.component.html',
    styleUrl: './update-employee.component.scss'
})
export class UpdateEmployeeComponent implements OnInit {
    userRegistered = false;
    loading = false;

    constructor(
        private readonly authService: AuthService,
        private readonly router: Router,
        private route: ActivatedRoute,
        private store: Store
    ) {}

    form!: FormGroup;

    onSubmit() {
        this.loading = true;
        this.authService.changePassword(this.id!, this.form.get('password')?.value).subscribe({
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

    id?: number;
    name = '';
    email = '';
    role = '';
    img = '';

    onCancel() {
        this.router.navigate(['/pages/admin']);
    }

    ngOnInit(): void {
        this.route.params.subscribe((p) => (this.id = p['id']));
        this.authService.getUserById(this.id!).subscribe((emp) => {
            this.email = emp.email;
            this.name = emp.name;
            this.role = emp.role;
            this.img = emp.img;
        });

        this.store.select(selectAuthState).subscribe((authState) => {
                    if(authState.user?.role !== 'MANAGER'){
                        this.router.navigate(['/'])
                    }
                })
        

        this.form = new FormGroup({
            password: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(20), Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/)]),
            confirmPassword: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(20), Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/), this.passwordMatchValidator()])
        });
    }

    private passwordMatchValidator() {
        return (control: FormControl): ValidationErrors | null => {
            const password = this.form?.get('password')?.value;
            const confirmPassword = control.value;
            return password && confirmPassword && password !== confirmPassword ? { valid: false } : null;
        };
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

    options = [
        { label: 'Manager', value: 'MANAGER' },
        { label: 'Employee', value: 'EMPLOYEE' }
    ];
}
