import { Component, OnInit, ViewChild, ElementRef, Renderer2  } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { HttpClient } from '@angular/common/http';
import { CarouselConfig } from 'ngx-bootstrap/carousel';
import { House } from '../house.interface';


@Component({
  selector: 'app-view-listing',
  templateUrl: './view-listing.component.html',
  styleUrls: ['./view-listing.component.css'],
  providers: [
    { provide: CarouselConfig, useValue: { interval: 0, wrap: false } }
  ]
})
export class ViewListingComponent implements OnInit {
  listing!: string;
  house!: House;
  @ViewChild('modalContactForm') modalContactForm!: ElementRef;
  name: string = '';
  // email: string = '';
  message: string = '';
  username: string|null = '';

  constructor(private http: HttpClient, private route: ActivatedRoute, private renderer: Renderer2, private router: Router) {}

  ngOnInit() { // Corrected method name to ngOnInit
    if(localStorage.getItem("userToken")!=null){
      this.username = localStorage.getItem("userToken");
    }
    else{
      this.router.navigate(['/login'])
    }
    this.route.params.subscribe(params => {
      this.listing = params['listing'];
    });
    // Use backticks to interpolate the dynamic value
    this.http.get<House>(`http://localhost:6077/realtor/view/${this.listing}`).subscribe(data => {
      this.house = data;
      console.log(data);
    });

  }

  openModal() {
    const modalElement = this.modalContactForm.nativeElement;
    this.renderer.addClass(modalElement, 'show');
    this.renderer.setStyle(modalElement, 'display', 'block');
  }

  closeModal() {
    const modalElement = this.modalContactForm.nativeElement;
    this.renderer.addClass(modalElement, 'show');
    this.renderer.setStyle(modalElement, 'display', 'none');    // Clear the form values
    this.name = '';
    // this.email = '';
    this.message = '';
  }
  
  submitForm(){
    console.log("CONTACT FORM SUBMIT");
    console.log(this.name);
    // console.log(this.email);
    console.log(this.message);
    if(localStorage.getItem("userToken")!=null){
      this.username = localStorage.getItem("userToken");
    }
    else{
      this.router.navigate(['/login'])
    }
    this.http.post<any>(`http://localhost:6077/realtor/contact`, { name: this.name, fromUsername: this.username, toUsername:this.house.seller, message: this.message, listing:this.listing })
    .subscribe(
      (response) => {
        console.log(response);
        // Handle the response from the backend here
        if (response && response.message) {
          // The request was successful, and the backend returned a message
          if (response.message == "email sent") {
            alert(response.message);
            this.closeModal();
          }
        } else {
          // The response may not contain a message, handle other scenarios here
        }
      }
    )
  }
  
  getS3ImageUrl(objectKey: string): string {
    const bucketName = 'realtorappimages';
    return `https://${bucketName}.s3.amazonaws.com/${objectKey}`;
  }
}
