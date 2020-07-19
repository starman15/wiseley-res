import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { throwError as observableThrowError,  Observable ,  Observer ,  Subject ,  of ,  interval } from 'rxjs';

import { Restaurant } from './reservation-model';

@Injectable({
	providedIn: 'root'
})
export class ReservationService {
	
	private restaurant: Restaurant;

	private baseUrl: string;

	constructor(private http: HttpClient) { 
		this.restaurant = new Restaurant('chipotle');
		this.baseUrl = 'api/v1/reservation';
	}
	
	getAvailability(dt: Date): Observable<Date[]> {
		let url = `${this.baseUrl}/availabilities/${this.restaurant.name}/${dt.getFullYear()}/${dt.getMonth()}/${dt.getDate()}`;
		
		return this.http.get<Date[]>(url);
	}
	
}
