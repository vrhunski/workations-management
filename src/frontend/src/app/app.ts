import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';

import {SidebarComponent} from './core/layout/sidebar/sidebar.component';
import {HeaderComponent} from './core/layout/header/header.component';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, SidebarComponent, HeaderComponent],
  templateUrl: './app.html',
  styleUrls: ['./app.scss']
})
export class App {}
