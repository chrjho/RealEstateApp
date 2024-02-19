import { HttpClient } from '@angular/common/http';
import { Component,OnInit} from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  currentUser: string|null = '';

  constructor(private http: HttpClient,private router: Router) { }
  ngOnInit(){
    if(localStorage.getItem("userToken")!=null){
      this.currentUser = localStorage.getItem("userToken");
    }
    else{
      this.router.navigate(['/login'])
    }
  }

  redirectToSell(){
    this.router.navigate(['/sell'])
  }

  redirectToBuy(){
    this.router.navigate(['/buy'])
  }
}
