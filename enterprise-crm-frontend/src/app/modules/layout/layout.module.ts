import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LayoutComponent } from '../../layout/layout.component';
import { FloatingConfiguratorComponent } from '../../layout/components/floating-configurator/floating-configurator.component';
import { FooterComponent } from '../../layout/components/footer/footer.component';
import { MenuComponent } from '../../layout/components/menu/menu.component';
import { MenuItemComponent } from '../../layout/components/menu-item/menu-item.component';
import { SidebarComponent } from '../../layout/components/sidebar/sidebar.component';
import { TopbarComponent } from '../../layout/components/topbar/topbar.component';
import { FormsModule } from '@angular/forms';
import { SelectButtonModule } from 'primeng/selectbutton';
import { AppConfigurator } from '../../layout/components/configurator/configurator.component';
import { ButtonModule } from 'primeng/button';
import { StyleClassModule } from 'primeng/styleclass';
import { RippleModule } from 'primeng/ripple';
import { Avatar } from 'primeng/avatar';
import { RouterModule } from '@angular/router';
import { LayoutService } from '../../layout/service/layout.service';

@NgModule({
    declarations: [LayoutComponent, FloatingConfiguratorComponent, FooterComponent, MenuComponent, MenuItemComponent, SidebarComponent, TopbarComponent, AppConfigurator],
    imports: [CommonModule, FormsModule, SelectButtonModule, ButtonModule, StyleClassModule, RippleModule, Avatar, RouterModule],
    exports: [LayoutComponent, FloatingConfiguratorComponent, AppConfigurator],
    providers: [LayoutService]
})
export class LayoutModule {}
