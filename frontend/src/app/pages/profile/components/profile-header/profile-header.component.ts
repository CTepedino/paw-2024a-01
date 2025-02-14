import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from '@angular/router';

@Component({
    selector: 'app-profile-header',
    standalone: true,
    imports: [CommonModule, MatButtonModule, RouterModule],
    templateUrl: './profile-header.component.html',
    styleUrl: './profile-header.component.scss'
})
export class ProfileHeaderComponent {
    @Input() user: any;
}