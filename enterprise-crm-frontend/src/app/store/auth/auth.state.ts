import { Employee } from '../../models/Employee';

export interface AuthState {
    user: Employee | null;
}

export const initialAuthState: AuthState = {
    user: sessionStorage.getItem('user') ? JSON.parse(sessionStorage.getItem('user')!) : null
};
