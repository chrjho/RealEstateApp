import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { SignupComponent } from './signup/signup.component';
import { AppRoutingModule } from './app-routing.module';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { NavbarComponent } from './navbar/navbar.component';
import { BuyComponent } from './buy/buy.component';
import { SellComponent } from './sell/sell.component';
import { MapComponent } from './map/map.component';
import { ListingComponent } from './listing/listing.component';
import { ImagesComponent } from './images/images.component';
import { MylistingsComponent } from './mylistings/mylistings.component';
import { CarouselModule } from 'ngx-bootstrap/carousel';
import { ViewListingComponent } from './view-listing/view-listing.component';
import { EmailsComponent } from './emails/emails.component';


@NgModule({
  declarations: [
    // Component declarations
    AppComponent,
    SignupComponent,
    HomeComponent,
    LoginComponent,
    NavbarComponent,
    BuyComponent,
    SellComponent,
    MapComponent,
    ListingComponent,
    ImagesComponent,
    MylistingsComponent,
    ViewListingComponent,
    EmailsComponent
  ],  
  imports: [
    BrowserModule,
    FormsModule, // Add this line
    HttpClientModule, // Add this line
    ReactiveFormsModule,
    AppRoutingModule,
    CarouselModule.forRoot(),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
