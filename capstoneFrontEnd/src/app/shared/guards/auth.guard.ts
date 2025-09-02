// src/app/core/guards/auth.guard.ts
import {inject, Injectable} from '@angular/core';
import {CanActivate, CanActivateFn, Router} from '@angular/router';
import { TokenService } from '../services/token.service';
import {AuthService} from '../services/auth.service';

export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);
  return auth.isAuthenticated() ? true : router.createUrlTree(['/login']);
};
@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private tokens: TokenService, private router: Router) {}
  canActivate(): boolean {
    if (this.tokens.isAuthenticated()) return true;
    this.router.navigate(['/login']);
    return false;
  }
}
