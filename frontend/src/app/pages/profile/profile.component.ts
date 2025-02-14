import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProfileHeaderComponent } from './components/profile-header/profile-header.component';
import { ProfileTabsComponent } from './components/profile-tabs/profile-tabs.component';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule,
    ProfileHeaderComponent,
    ProfileTabsComponent
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent {
  user = {
    userId: 1,
    firstName: 'Federico',
    lastName: 'Madero',
    email: 'emaderotorres@gmail.com',
    writerCategory: 'BRONZE',
    cbu: '111122223333444555566',
    isWriter: true
  };

  selectedTab = 'publications';
}