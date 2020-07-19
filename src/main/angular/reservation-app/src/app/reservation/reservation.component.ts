import { Component, OnInit } from '@angular/core';
import { NgForm, FormGroup, FormControl, FormBuilder, Validators, ValidatorFn } from '@angular/forms';
import { NgbDate, NgbDateStruct, NgbCalendar} from '@ng-bootstrap/ng-bootstrap';
import * as moment from 'moment';

@Component({
	selector: 'app-reservation',
	templateUrl: './reservation.component.html',
	styleUrls: ['./reservation.component.css']
})
export class ReservationComponent implements OnInit {

	editForm: FormGroup;

	partySizes: number[] = [ 1, 2, 3, 4, 5, 6, 7, 8,  9, 10 ];

	constructor(private fb: FormBuilder) {

	}

	ngOnInit() {
		this.initForm();
	}

	initForm(): void {
		
		this.editForm = this.fb.group({
			'name' : '',
			'email': '',
			'partySize': [ 1, Validators.pattern('[0-9]*')],
			'date': null,
			'time': null
		});
	}
	
	
	// Choose city using select dropdown
	onChangePartySize(e) {
		console.log(e.target.value);
		this.editForm.controls['partySize'].setValue(e.target.value);
	}	
	

	onDateSelect(date: NgbDate) {
		let mdate = moment().year(date.year).month(date.month-1).date(date.day);
		this.editForm.controls['date'].setValue(mdate.toDate());
	}
	
}
