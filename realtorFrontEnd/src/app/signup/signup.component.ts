import { Component } from '@angular/core';
import { UserResponse } from '../user-response.interface';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {
  username: string = '';
  password: string = '';
  email: string = '';

  constructor(private http: HttpClient) { }

  submitForm() {
    this.http.post<UserResponse>('http://localhost:6077/realtor/signup', { username: this.username, email: this.email, password: this.password })
      .subscribe(
        (response) => {
          console.log(response);
          // Handle the response from the backend here
          if (response && response.message) {
            // The request was successful, and the backend returned a message
            if (response.message == "User successfully signed up.") {
              alert(response.message);
            }
          } else {
            // The response may not contain a message, handle other scenarios here
          }
        }
        //   {
        //   next: () => {
        //     console.log('Text sent to backend');
        //     this.username = ''; // Clear the input field after successful submission
        //     this.password = ''; // Clear the input field after successful submission
        //   },
        //   error: (error) => {
        //     console.error('Error occurred:', error);
        //   }
        // }
      )
  }
}
