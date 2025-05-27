import { inject, Injectable } from '@angular/core';
import { Actions, ofType, createEffect } from '@ngrx/effects';
import { loginSucess, logout } from './auth.actions';
import { tap } from 'rxjs/operators';
import { Store } from '@ngrx/store'; // Import Store if you need it

@Injectable()
export class AuthEffects {
    actions$ = inject(Actions);
    store = inject(Store);
    storeUser$ = createEffect(
        () =>
            this.actions$.pipe(
                ofType(loginSucess),
                tap(({ user }) => {
                    sessionStorage.setItem('user', JSON.stringify(user));
                })
            ),
        { dispatch: false }
    );

    clearUser$ = createEffect(
        () =>
            this.actions$.pipe(
                // Access actions$ via the getter
                ofType(logout),
                tap(() => {
                    sessionStorage.removeItem('user');
                })
            ),
        { dispatch: false }
    );
}
