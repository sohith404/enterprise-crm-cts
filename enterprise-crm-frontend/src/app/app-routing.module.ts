import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { appRoutes } from './app.routes';

const routes: Routes = appRoutes;

@NgModule({
    imports: [
        RouterModule.forRoot(routes, {
            scrollPositionRestoration: 'enabled',
            anchorScrolling: 'enabled',
            initialNavigation: 'enabledBlocking'
        })
    ],
    exports: [RouterModule]
})
export class AppRoutingModule {}
