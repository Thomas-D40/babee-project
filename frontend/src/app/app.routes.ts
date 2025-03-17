import { Routes } from '@angular/router';
import { AddBabeeComponent } from './babee/add-babee/add-babee.component';
import { BabeeListComponent } from './babee/babee-list/babee-list.component';
import { BabeeProfileComponent } from './babee/babee-profile/babee-profile.component';
import { LoginComponent } from './login/login/login.component';

export const routes: Routes = [
  {
    path: 'bebe',
    children: [
      {
        path: 'add',
        component: AddBabeeComponent,
        title: 'Ajouter un Babee',
      },
      {
        path: ':id',
        component: BabeeProfileComponent,
        title: 'Babee',
        // canActivate: [AuthGuard],
      },
      {
        path: '',
        component: BabeeListComponent,
        // canActivate: [AuthGuard],
      },
    ],
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: '**',
    redirectTo: 'bebe',
    pathMatch: 'full',
  },
];
