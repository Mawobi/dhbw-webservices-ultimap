import {Component, OnInit} from '@angular/core';
import {INavItem} from '../../types/INavItem';

@Component({
  selector: 'app-navbar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent implements OnInit {
  public navItems: INavItem[] = [
    {
      title: 'Home',
      href: '/',
      icon: 'home'
    },
  ];

  constructor() {
  }

  ngOnInit(): void {
  }

}
