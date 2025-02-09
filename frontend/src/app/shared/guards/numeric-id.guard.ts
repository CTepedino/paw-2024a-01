import { CanActivateFn } from '@angular/router';

export const numericIDGuard: CanActivateFn = (route, state) => {
    const id = route.params['id'];
    const regex = /^[0-9]+$/;

    return regex.test(id);
};
