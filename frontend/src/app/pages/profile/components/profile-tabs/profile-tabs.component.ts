import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
    selector: 'app-profile-tabs',
    standalone: true,
    imports: [CommonModule, RouterModule],
    templateUrl: './profile-tabs.component.html',
    styleUrl: './profile-tabs.component.scss'
})
export class ProfileTabsComponent {
    @Input() selectedTab: string = 'publications';
    @Output() tabChange = new EventEmitter<string>();

    onTabClick(tab: string) {
        this.tabChange.emit(tab);
    }
}