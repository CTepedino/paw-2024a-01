import {Component, input} from '@angular/core';
import {MatMenu, MatMenuTrigger} from "@angular/material/menu";
import {MatButton} from "@angular/material/button";
import {MatCardAvatar} from "@angular/material/card";
import {NgOptimizedImage} from "@angular/common";
import {User} from "../../model/user/user";

@Component({
  selector: 'app-navbar-menu',
  imports: [
    MatMenu,
    MatMenuTrigger,
    MatButton,
    MatCardAvatar,
    NgOptimizedImage
  ],
  templateUrl: './navbar-menu.component.html',
  styleUrl: './navbar-menu.component.scss'
})
export class NavbarMenuComponent {
  user = input.required<Partial<User> | null>();
  pfp = input.required<string>();

  getPfp(){
    if (this.user() == null){
      return 'assets/user.jpeg';
    }
    return `${this.user()?.profilePicture}?height=50&width=50&t=${new Date().getTime()}`;
  }


}
