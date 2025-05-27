import { createSelector } from '@ngrx/store';
import { selectAuthState } from './auth/auth.selector';
import { AuthState } from './auth/auth.state';
import { selectTimeState } from './time/time.selectors';

export const selectCurrentUser = createSelector(selectAuthState, (state: AuthState) => state.user);

export const selectCurrentTime = createSelector(selectTimeState, (state) => state.currentTime);
