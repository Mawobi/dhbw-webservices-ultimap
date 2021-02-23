import {Component, Input, OnInit} from '@angular/core';
import {INavItem} from '../../types/INavItem';

@Component({
  selector: 'app-nav-tab',
  templateUrl: './nav-tab.component.html',
  styleUrls: ['./nav-tab.component.scss']
})
export class NavTabComponent implements OnInit {
  @Input() item: INavItem | undefined;

  constructor() {
  }

  ngOnInit(): void {
  }

}
