import { Component } from '@angular/core';
import { GeneralInfoComponent } from './components/general-info/general-info.component';
import { ModulesComponent } from './components/modules-component/modules.component';

@Component({
    selector: 'app-landing',
    standalone: false,
    templateUrl: './landing.component.html',
    styleUrl: './landing.component.scss'
})
export class LandingComponent {
    title = 'Dashboard';
    date = new Date();
    time = this.date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    dateString = this.date.toLocaleDateString([], { month: '2-digit', day: '2-digit', year: 'numeric' });
    component = GeneralInfoComponent;
    tabs = [
        { label: 'General Info', content: GeneralInfoComponent },
        { label: 'Services', content: ModulesComponent }
    ];
}
