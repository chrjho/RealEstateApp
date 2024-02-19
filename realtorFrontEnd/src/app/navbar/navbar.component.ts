import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  constructor(private router: Router, private route: ActivatedRoute) {}
  logout(): void {
    // Clear localStorage
    localStorage.clear();
    // Redirect to login page or perform any other actions after logout
    this.router.navigate(['/login']); // Example: Redirect to login page
  }
}
