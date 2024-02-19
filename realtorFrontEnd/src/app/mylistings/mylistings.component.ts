import { HttpClient, HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CarouselConfig } from 'ngx-bootstrap/carousel';

@Component({
  selector: 'app-mylistings',
  templateUrl: './mylistings.component.html',
  styleUrls: ['./mylistings.component.css'],
  providers: [
    { provide: CarouselConfig, useValue: { interval: 0, wrap: false } }
  ]
})
export class MylistingsComponent implements OnInit {
  currentUser : any
  houses :any[] = [];
  selectedImages: File[] = [];

  constructor(private http: HttpClient, private router: Router) {}
  ngOnInit(){
    if(localStorage.getItem("userToken")!=null){
      this.currentUser = localStorage.getItem("userToken");
      let httpParams = new HttpParams();
      httpParams = httpParams.set("username",this.currentUser);
      this.http.get<any[]>('http://localhost:6077/realtor/mylistings',{params: httpParams}).subscribe(
        (data) => {
          this.houses = data;
          console.log(this.houses);
        },
        (error) => {
          console.error('Error fetching listings:', error);
        }
      )
    }
    else{
      this.router.navigate(['/login'])
    }
  }

  updateListing(house: any) {
    const formData = new FormData();

    // Append updated fields
    formData.append('listing', house.listing);
    formData.append('price', house.price);
    formData.append('beds', house.beds);
    formData.append('baths', house.baths);
    formData.append('size', house.size);

    // Append images
    if(this.selectedImages.length != 0){
      for (const image of this.selectedImages) {
        formData.append('images', image);
      }

      // Send the PUT request with updated fields and images
      this.http.post('http://localhost:6077/realtor/edit-listing', formData)
        .subscribe(
          (response) => {
            console.log('Listing updated:', response);
            // Handle success if needed
            location.reload();
          },
          (error) => {
            console.error('Error updating listing:', error);
            // Handle error if needed
          }
        );
    }
    else{
      this.http.post('http://localhost:6077/realtor/edit-listing-no-image', formData)
      .subscribe(
        (response) => {
          console.log('Listing updated:', response);
          // Handle success if needed
          location.reload();
        },
        (error) => {
          console.error('Error updating listing:', error);
          // Handle error if needed
        }
      );
    }
  }
  
  deleteListing(house: any){
    this.http.post('http://localhost:6077/realtor/delete', { listing: house.listing }).subscribe(
      (response) => {
        console.log('Listing deleted successfully:', response);
        // Handle success if needed
        location.reload();
      },
      (error) => {
        console.error('Error deleting listing:', error);
        // Handle error if needed
      }
    );
  }
  onImagesSelected(event: any) {
    this.selectedImages = event.target.files;
  }

  getS3ImageUrl(objectKey: string): string {
    const bucketName = 'realtorappimages';
    return `https://${bucketName}.s3.amazonaws.com/${objectKey}`;
  }

}
