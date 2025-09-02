import { Routes } from '@angular/router';
import {AppHomeComponent, landingRedirectGuard} from './app-home/app-home.component';
import { SearchComponent } from './view-compare/search/search.component';
import { LoginComponent } from './user/login/login.component';
import { RegisterComponent } from './user/register/register.component';
import { UserAccountComponent } from './user/user-account/user-account.component';
import { AccountEditComponent } from './user/account-edit/account-edit.component';
import { UserBillsComponent } from './user/user-bills/user-bills.component';
import { RateInputComponent } from './crowdsource/rate-input/rate-input.component';
import { AdminHomeComponent } from './admin/admin-home/admin-home.component';
import { AdminApprovalComponent } from './admin/admin-approval/admin-approval.component';
import { AuthGuard } from './shared/guards/auth.guard';
import {AddBillComponent} from './user/user-account/add-bill/add-bill.component';

export const routes: Routes = [
  { path: '', component: AppHomeComponent },
  { path: 'compare', component: SearchComponent },
  { path: 'login', component: LoginComponent },
  { path: '', canActivate: [landingRedirectGuard], pathMatch: 'full', component: LoginComponent },


  { path: 'login', component: LoginComponent },

  { path: 'register', component: RegisterComponent },

  {
    path: 'user-account',
    component: UserAccountComponent,
    canActivate: [AuthGuard],
    children: [

      { path: '', pathMatch: 'full', component: AccountEditComponent },

      { path: 'bills',
        component: UserBillsComponent, children: [{path:'add-bill', component: AddBillComponent}]},


      { path: 'crowdsource', component: RateInputComponent },
      { path: 'compare', component: SearchComponent },
    ]
  },


   { path: 'crowdsource_form', component: RateInputComponent },

  {
    path: 'admin',
    component: AdminHomeComponent,
    children: [
      { path: 'approval', component: AdminApprovalComponent },
    ]
  },

  { path: '**', redirectTo: '' }
];
