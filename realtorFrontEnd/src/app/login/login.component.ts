import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { UserResponse } from '../user-response.interface';
import { AppComponent } from '../app.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  usernameOrEmail: string = '';
  password: string = '';

  constructor(private http: HttpClient,private router: Router) { }

  submitForm() {
    if(this.usernameOrEmail.includes("@")){
      this.http.post<UserResponse>('http://localhost:6077/realtor/login', { email: this.usernameOrEmail, password: this.password })
      .subscribe(
        (response) => {
          console.log(response);
          // Handle the response from the backend here
          if (response && response.message) {
            // The request was successful, and the backend returned a message
            if (response.message.includes("User successfully logged in: ")) {
              localStorage.setItem('userToken',response.message.split('User successfully logged in: ')[1]);
              // alert(response.message);
              this.router.navigate(['/home'])
            }else{
              alert(response.message);
            }
          }
        }
      )
    }
    else{
      this.http.post<UserResponse>('http://localhost:6077/realtor/login', { username: this.usernameOrEmail, password: this.password })
      .subscribe(
        (response) => {
          console.log(response);
          // Handle the response from the backend here
          if (response && response.message) {
            // The request was successful, and the backend returned a message
            if (response.message.includes("User successfully logged in: ")) {
              localStorage.setItem('userToken',this.usernameOrEmail);
              alert(response.message);
              this.router.navigate(['/home'])
              
            }else{
              alert(response.message);
            }
          }
        }
      )
    }
  }
}
