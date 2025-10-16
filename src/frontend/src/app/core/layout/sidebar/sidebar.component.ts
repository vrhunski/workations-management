import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatIconModule } from '@angular/material/icon';


@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule, MatTooltipModule, MatIconModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {
  collapsed = false;

  toggleSidebar(): void {
    this.collapsed = !this.collapsed;
    console.log('Sidebar collapsed:', this.collapsed);
  }

  menuItems = [
  { name: 'Workations', route: '/workations', icon: 'group' },
  { name: 'About', route: '/about', icon: 'query_stats' }
];
}
