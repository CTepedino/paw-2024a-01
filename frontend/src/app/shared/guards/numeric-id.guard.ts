import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";

export const numericIDGuard: CanActivateFn = (route, state) => {
    const router = inject(Router)

    const id = route.params['id'];
    const regex = /^[0-9]+$/;

    if (!regex.test(id)){
        router.navigate(['/']);
        return false;
    }

    return true;
};
