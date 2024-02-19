import {NgModule} from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SignupComponent } from './signup/signup.component';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { BuyComponent } from './buy/buy.component';
import { SellComponent } from './sell/sell.component';
import { MapComponent } from './map/map.component';
import { ImagesComponent } from './images/images.component';
import { MylistingsComponent } from './mylistings/mylistings.component';
import { ViewListingComponent } from './view-listing/view-listing.component';
import { EmailsComponent } from './emails/emails.component';

const routes: Routes = [
    {
        path: '', component: HomeComponent
    },
    {
        path: 'signup', component: SignupComponent
    },
    {
        path: 'login', component: LoginComponent
    },
    {
        path: 'home', component: HomeComponent
    },
    {
        path: 'buy', component: BuyComponent
    },
    {
        path: 'sell', component: SellComponent
    },
    {
        path: 'images', component: ImagesComponent
    },
    {
        path: 'mylistings', component:MylistingsComponent
    },
    {
        path: 'view/:listing', component:ViewListingComponent
    },
    {
        path: 'messages', component:EmailsComponent
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})

export class AppRoutingModule {}