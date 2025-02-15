import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../services/auth.service";
import {OrderService} from "../services/order.service";
import {concatMap, map, of} from "rxjs";

export const canBuyBookGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const orderService = inject(OrderService);
  const router = inject(Router);

  const id = route.paramMap.get('id');

  return authService.getLoggedUser().pipe(
      concatMap(user => {
        if( user?.id == Number(id) ){
          router.navigate(['/']);
          return of(false);
        }
        return orderService.listOrders({
          buyer_id: user?.id,
          book_id: Number(id)
        }).pipe(
            map((orders) => {
              if(orders.pagination.totalCount != 0){
                router.navigate(['/']);
                return false;
              }
              return true;
            })
        );
      })
  );
};
