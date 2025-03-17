import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export const AuthGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const user = authService.getUser();

  if (!user) {
    router.navigate(['/login']);
    return false;
  }

  if (authService.isCrecheStaff()) {
    router.navigate(['/bebe']);
  } else if (authService.isParent()) {
    router.navigate([`/bebe/${authService.getBebeId()}`]);
  }

  return true;
};
