import {Component, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html'
})
export class NavbarComponent implements OnInit {

  showSideNavToggle = false;
  username: string;
  sidenavToggleSubscription: Subscription;
  showSideNav = false;

  constructor() {
  }

  ngOnInit() {

  }

}
