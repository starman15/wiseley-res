import { Injectable } from '@angular/core';

import { Restaurant } from './reservation-model';

@Injectable({
	providedIn: 'root'
})
export class ReservationService {
	
	private restaurant: Restaurant;

	constructor() { 
		this.restaurant = new Restaurant('chipotle');
	}
	
	getAvailability(ondate: Date) {
		
	}
}
