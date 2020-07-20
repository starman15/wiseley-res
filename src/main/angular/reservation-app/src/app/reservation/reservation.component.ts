import { Component, OnInit } from '@angular/core';
import { NgForm, FormGroup, FormControl, FormBuilder, Validators, ValidatorFn } from '@angular/forms';
import { NgbDate, NgbDateStruct, NgbCalendar} from '@ng-bootstrap/ng-bootstrap';
import * as moment from 'moment';

import { ReservationService } from './reservation.service';
import { Diner } from './reservation-model';


@Component({
	selector: 'app-reservation',
	templateUrl: './reservation.component.html',
	styleUrls: ['./reservation.component.css']
})
export class ReservationComponent implements OnInit {

	editForm: FormGroup;

	partySizes: number[] = [ 1, 2, 3, 4, 5, 6, 7, 8,  9, 10 ];

	done: boolean;
	taken: boolean;

	times: Date[] = [
//		new Date("2020-07-20T12:00:00-04:00"),
//		new Date("2020-07-20T12:15:00-04:00"),
//		new Date("2020-07-20T12:30:00-04:00"),
//		new Date("2020-07-20T12:45:00-04:00"),
//		new Date("2020-07-20T14:00:00-04:00"),
//		new Date("2020-07-20T14:15:00-04:00"),
//		new Date("2020-07-20T14:30:00-04:00"),
//		new Date("2020-07-20T14:45:00-04:00")
	];

	constructor(private fb: FormBuilder,
			private reservationService: ReservationService) {
		this.done = false;
		this.taken = false;
	}

	ngOnInit() {
		this.initForm();
	}

	initForm(): void {
		
		this.editForm = this.fb.group({
			'name' : '',
			'email': '',
			'partySize': [ null, Validators.pattern('[0-9]*')],
			'date': null,
			'time': null
		});
	}
	
	formatTime(dt: Date) {
		let s =  moment(dt).format('h:mmA');
		return s;
	}
	
	get isFormValid(): boolean {
		return this.editForm 
			&& this.editForm.valid
			 && this.editForm.controls['partySize'].value
			 && this.editForm.controls['date'].value
			 && this.editForm.controls['time'].value;
	}
	
	
	onChangePartySize(e) {
		console.log(e.target.value);
		this.editForm.controls['partySize'].setValue(e.target.value);
	}	
	
	onChangeTime(e) {
		console.log(e);
		this.editForm.controls['time'].setValue(e);
	}	
	
	makeReservation() {
		const diner = new Diner(this.editForm.controls['name'].value, this.editForm.controls['email'].value);
		const dateTime = this.editForm.controls['time'].value;
		const partySize = this.editForm.controls['partySize'].value;
		
		this.taken = false;
		this.reservationService.makeReservation(diner, dateTime, partySize)
			.subscribe(() => this.done = true,
					error => {
						this.taken = true;
						let date = this.editForm.controls['date'].value;
						this.reservationService.getAvailability(date)
							.subscribe(times => this.times = times);
					});
	}	

	onDateSelect(date: NgbDate) {
		let mdate = moment().year(date.year).month(date.month-1).date(date.day);
		this.editForm.controls['date'].setValue(mdate.toDate());
		
		this.reservationService.getAvailability(mdate.toDate())
			.subscribe(times => this.times = times);
	}
	
	get isDateSelected(): boolean {
		let date = this.editForm.controls['date'].value;
		return (date) ? true : false;
	}
	
}
