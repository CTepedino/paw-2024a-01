import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {OrderSearchQuery} from "../model/order/orderSearchQuery";
import {Order} from "../model/order/order";
import {Observable} from "rxjs";
import {environment} from "../../../enviroment/enviroment";
import {MediaTypes} from "../const/mediaTypes";

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private apiURL = `${environment.apiURL}/orders`

  constructor(private http: HttpClient) { }

  listOrders(query: OrderSearchQuery): Observable<Order[]>{
    const params = new HttpParams();

    Object.entries(query).forEach(([name, value]) => {
      if (value !== null && value !== undefined){
        params.append(name, value);
      }
    })

    return this.http.get<Order[]>(this.apiURL, {params: params})
  }

  postOrder(order: Order){
    this.http.post(
        this.apiURL,
        order,
        {headers: {"Content-Type": MediaTypes.ORDER}}
    );
  }

  getOrder(orderUrl: string): Observable<Order>{
    return this.http.get<Order>(orderUrl);
  }

  patchOrder(orderUrl: string, rejectionReason: string | null){
    return this.http.patch(
        orderUrl,
        {rejectionReason: rejectionReason},
        {headers: {"Content-Type": MediaTypes.ORDER}}
    );
  }

  putReceipt(orderUrl: string, receipt: File){
    const formData = new FormData();
    formData.append("receipt", receipt);
    this.http.put(`${orderUrl}/receipt`, formData);
  }
}
