import { Component, computed, inject } from '@angular/core';
import { LayoutService } from '../../service/layout.service';

@Component({
    selector: 'app-floating-configurator',
    standalone: false,
    templateUrl: './floating-configurator.component.html',
    styleUrl: './floating-configurator.component.scss'
})
export class FloatingConfiguratorComponent {
    LayoutService = inject(LayoutService);

    isDarkTheme = computed(() => this.LayoutService.layoutConfig().darkTheme);

    toggleDarkMode() {
        this.LayoutService.layoutConfig.update((state) => ({ ...state, darkTheme: !state.darkTheme }));
    }
}
