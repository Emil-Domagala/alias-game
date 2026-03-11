import { inject } from '@angular/core';
import { ResolveFn, Router } from '@angular/router';
import { UserService } from '../user/user.service';
import {AUTH_ROUTES_FULL} from './auth.routes';

export const authResolver: ResolveFn<boolean> = async () => {
  const userService = inject(UserService);
  const router = inject(Router);

  const user = await userService.fetchUser();

  if (!user) {
    router.navigate(AUTH_ROUTES_FULL.LOGIN);
    return false;
  }

  return true;
};
