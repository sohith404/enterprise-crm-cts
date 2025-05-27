import { Component, Input } from '@angular/core';

@Component({
    selector: 'app-tabs',
    templateUrl: './tabs.component.html',
    styleUrls: ['./tabs.component.scss'],
    standalone: false
})
export class TabsComponent {
    @Input() tabs: { label: string; content: string | any }[] = [];
    activeIndex = 0;
    setActive(index: number) {
        this.activeIndex = index;
    }
}
