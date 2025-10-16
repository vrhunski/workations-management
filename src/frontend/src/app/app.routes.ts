import { Routes } from '@angular/router';
import { AboutComponent } from './about/about.component';
import {WorkationsComponent} from './workations/workations.component';

export const routes: Routes = [
  { path: '', redirectTo: '/workations', pathMatch: 'full' },
  { path: 'workations', component: WorkationsComponent },
  { path: 'about', component: AboutComponent },
];
