import { Component, HostListener } from '@angular/core';
import { UserResponse } from '../user-response.interface';
import { HttpClient } from '@angular/common/http';
import { Router, NavigationExtras } from '@angular/router';

@Component({
  selector: 'app-sell',
  templateUrl: './sell.component.html',
  styleUrls: ['./sell.component.css']
})
export class SellComponent {
  constructor(private http: HttpClient,private router: Router) {
    if(localStorage.getItem("userToken")==null){
      this.router.navigate(['/login'])
    }
   }
  // Handler for the iframe load event
  onIframeLoad() {
    console.log('Iframe loaded');
    // Assuming you have a reference to the iframe element (e.g., using ViewChild)
    const iframeElement = document.querySelector('iframe') as HTMLIFrameElement;

    // Attach a click event listener to the iframe's submit button
    iframeElement.contentWindow?.document
      .querySelector('.button-cta')
      ?.addEventListener('click', () => this.handleIframeSubmit());
  }

  handleIframeSubmit() {
    console.log('Submit button clicked in the iframe');
  }

  @HostListener('window:message', ['$event'])
  onMessage(event: MessageEvent) {
    const iframeElement = document.querySelector('iframe') as HTMLIFrameElement;
    // Check if the message is from the iframe and contains form data
    if (event.source === iframeElement.contentWindow && event.data) {
      const data = event.data; // Data sent from the iframe
      console.log(data); // Use the data as needed
      this.http.post<UserResponse>('http://localhost:6077/realtor/sell',  data )
      .subscribe(
        (response) => {
          console.log(response);
          // Handle the response from the backend here
          if (response && response.message) {
              console.log(response.message);
              const encodedListingId = encodeURIComponent(response.message);
              const queryParams: any = { listing: encodedListingId };
              const navigationExtras: NavigationExtras = {
                  queryParams,
              };

              this.router.navigate(['/images'], navigationExtras);          
          }
        }
      )
    }
  }
}
