import { createReducer, on } from '@ngrx/store';
import { initialAuthState } from './auth.state';
import { loginSucess, logout } from './auth.actions';

export const authReducer = createReducer(
    initialAuthState,
    on(loginSucess, (state, { user }) => ({ ...state, user })),
    on(logout, (state) => ({ ...state, user: null }))
);
