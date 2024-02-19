import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { email } from '../email.interface';

@Component({
  selector: 'app-emails',
  templateUrl: './emails.component.html',
  styleUrls: ['./emails.component.css']
})
export class EmailsComponent {
  currentUser: string|null = '';
  emails!: email[];

  constructor(private http: HttpClient, private router: Router) { }

  ngOnInit(){
    if(localStorage.getItem("userToken")!=null){
      this.currentUser = localStorage.getItem("userToken");
    }
    else{
      this.router.navigate(['/login'])
    }
    console.log(this.currentUser);
    this.http.post<any>(`http://localhost:6077/realtor/messages`,{username: this.currentUser}).subscribe(data => {
      // Group emails by thread and sort them by timestamp
      this.emails = this.processEmails(data);
    });
  }

  processEmails(data: any): email[] {
    // Initialize an empty object to store grouped emails
    const groupedEmails: { [key: string]: email[] } = {};

    // Group emails by thread
    data.forEach((email: email) => {
      (groupedEmails[email.thread] = groupedEmails[email.thread] || []).push(email);
    });

    // Sort emails within each thread by timestamp in descending order
    for (const key in groupedEmails) {
      groupedEmails[key].sort((a, b) => (new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime()));
    }

    // Create an array to hold the grouped emails
    const groupedEmailsArray: email[] = [];
    // Push each group of emails into the array
    for (const key in groupedEmails) {
      groupedEmailsArray.push(...groupedEmails[key]);
    }

    // Return the array of grouped and sorted emails
    return groupedEmailsArray;
  }
}
