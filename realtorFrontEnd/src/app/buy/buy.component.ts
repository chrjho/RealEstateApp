import { HttpClient, HttpParams } from '@angular/common/http';
import { Component } from '@angular/core';
import { HouseData } from '../house-data.interface';
import { forkJoin, map } from 'rxjs';
import { Router } from '@angular/router';
import { House } from '../house.interface';

@Component({
  selector: 'app-buy',
  templateUrl: './buy.component.html',
  styleUrls: ['./buy.component.css']
})
export class BuyComponent {
  houseData!: any[];
  listOfAddresses: string[] = [];
  listOfPictureUrls: string[] = [];
  sellers: string[] = [];
  selectedListing: string | null = null;
  currentUser: any;
  // house!: House;

  constructor(private http: HttpClient, private router: Router) {
    if(localStorage.getItem("userToken")!=null){
      this.currentUser = localStorage.getItem("userToken");
      let httpParams = new HttpParams();
      httpParams = httpParams.set("username",this.currentUser);
      this.http.get<any[]>('http://localhost:6077/realtor/buy',{params: httpParams}).subscribe(data => {
      this.houseData = data;
      console.log("housedata:"+this.houseData);
      const geocodeRequests = this.houseData.map((house, index) => {
        const address = house.address;
        this.sellers[index] = house.seller;
        const apiUrl = `https://maps.googleapis.com/maps/api/geocode/json?address=${encodeURIComponent(address)}&key=KEY`;
        return this.http.get<any>(apiUrl).pipe(map(response => ({ address, index, response })));
      });

      forkJoin(geocodeRequests).subscribe(
        (responses) => {
          responses.forEach(({ address, index, response }) => {
            if (response.status === 'OK' && response.results.length > 0) {
              const location = response.results[0].geometry.location;
              const latLng = `${location.lat},${location.lng}`;
              const imageUrl = `https://maps.googleapis.com/maps/api/streetview?size=1000x500&location=${latLng}&key=KEY`;
              this.listOfAddresses[index] = address;
              this.listOfPictureUrls[index] = imageUrl;
              this.router.navigate(['/buy'])
            } else {
              console.log('Geocoding was not successful for address:', address);
            }
          });
        },
        (error) => {
          console.error('Error:', error);
        }
      );
    });
    }
    else{
      this.router.navigate(['/login'])
    }
  }

  onListingClick(address: string) {
    this.selectedListing = address;
  }
}
