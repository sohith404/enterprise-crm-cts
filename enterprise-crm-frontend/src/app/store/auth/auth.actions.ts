import { createAction, props } from '@ngrx/store';
import { Employee } from '../../models/Employee';

export const loginSucess = createAction('[Auth] Login Success', props<{ user: Employee }>());
export const logout = createAction('[Auth] Logout');
