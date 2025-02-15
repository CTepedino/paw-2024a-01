import {Component, input, Input} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from '@angular/router';
import {UserRoles} from "../../../../shared/model/user/userRoles";
import {User} from "../../../../shared/model/user/user";
import {WriterCategory} from "../../../../shared/model/user/writerCategory";
import {NavButtonComponent} from "../../../../shared/components/nav-button/nav-button.component";

@Component({
    selector: 'app-profile-header',
    standalone: true,
    imports: [CommonModule, MatButtonModule, RouterModule, NavButtonComponent, NgOptimizedImage],
    templateUrl: './profile-header.component.html',
    styleUrl: './profile-header.component.scss'
})
export class ProfileHeaderComponent {

    user = input.required<User>();
    ownsProfile = input.required<boolean>();


	protected readonly UserRoles = UserRoles;
    protected readonly WriterCategory = WriterCategory;
}