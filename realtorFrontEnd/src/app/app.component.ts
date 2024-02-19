import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { UserResponse } from './user-response.interface';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor(private router: Router) {}

  isLoginPage(): boolean {
    return this.router.url === '/login';
  }

  isSignupPage(): boolean {
    return this.router.url === '/signup';
  }
  // username: string = '';
  // password: string = '';

  // constructor(private http: HttpClient) { }

  // submitForm() {
  //   this.http.post<UserResponse>('http://localhost:6077/realtor/signup', { username: this.username, password: this.password })
  //     .subscribe(
  //       (response) => {
  //         console.log(response);
  //         // Handle the response from the backend here
  //         if (response && response.message) {
  //           // The request was successful, and the backend returned a message
  //           if (response.message == "User successfully signed up") {
  //             alert(response.message);
  //           }
  //         } else {
  //           // The response may not contain a message, handle other scenarios here
  //         }
  //       }
  //       //   {
  //       //   next: () => {
  //       //     console.log('Text sent to backend');
  //       //     this.username = ''; // Clear the input field after successful submission
  //       //     this.password = ''; // Clear the input field after successful submission
  //       //   },
  //       //   error: (error) => {
  //       //     console.error('Error occurred:', error);
  //       //   }
  //       // }
  //     )
  // }
}