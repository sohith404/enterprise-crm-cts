import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { environment } from '../../../../environments/environment.development';
import { loginSucess, logout } from '../../../store/auth/auth.actions';
import { tap } from 'rxjs';
import { Router } from '@angular/router';
import { Employee } from '../../../models/Employee';
import { ErrorResponse } from '../../../models/ErrorResponse';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    constructor(
        private readonly httpClient: HttpClient,
        private readonly store: Store,
        private readonly router: Router
    ) {}

    registerUser(employee: Employee) {
        return this.httpClient.post<Employee>(environment.apiUrl + '/authentication', employee);
    }

    login(email: string, password: string) {
        return this.httpClient.post<Employee | ErrorResponse>(environment.apiUrl + '/authentication/user', { email, password }).pipe(
            tap((user) => {
                if ('id' in user && 'email' in user && 'password' in user) {
                    this.store.dispatch(loginSucess({ user }));
                    this.router.navigate(['/']);
                }
            })
        );
    }

    logout() {
        this.store.dispatch(logout());
        this.router.navigate(['/auth/login']);
    }

    getCurrentUser(): Employee | null {
        const currentUser = sessionStorage.getItem('user');
        return currentUser ? JSON.parse(currentUser) : null;
    }

    changePassword(id: number, newPassword: string) {
        return this.httpClient.patch<Employee>(environment.apiUrl + '/authentication/' + id + '/' + newPassword, null);
    }

    getUserById(id: number) {
        return this.httpClient.get<Employee>(environment.apiUrl + '/authentication/' + id);
    }

    getAllUser() {
        return this.httpClient.get<Employee[]>(environment.apiUrl + '/authentication');
    }

    deleteUser(id: number) {
        return this.httpClient.delete<{ message: string; status: string }>(environment.apiUrl + '/authentication/' + id);
    }
}
