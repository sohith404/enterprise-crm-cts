import { Component } from '@angular/core';
import { LayoutService } from '../../service/layout.service';
import { MenuItem } from 'primeng/api';

@Component({
    selector: 'app-topbar',
    standalone: false,
    templateUrl: './topbar.component.html',
    styleUrl: './topbar.component.scss'
})
export class TopbarComponent {
    items!: MenuItem[];

    constructor(public layoutService: LayoutService) {}

    toggleDarkMode() {
        this.layoutService.layoutConfig.update((state) => ({ ...state, darkTheme: !state.darkTheme }));
    }
}
