import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { ReservationComponent } from './reservation/reservation.component';
import { ReservationService } from './reservation/reservation.service';

@NgModule({
  declarations: [
    AppComponent,
    ReservationComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
	HttpClientModule,
    NgbModule,
    ReactiveFormsModule,
    AppRoutingModule
  ],
  providers: [
	  ReservationService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
