import { HttpClient, HttpParams } from '@angular/common/http';
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-images',
  templateUrl: './images.component.html',
  styleUrls: ['./images.component.css']
})
export class ImagesComponent {
  selectedImages: File[] = [];
  listingId: string = "";


  constructor(private http: HttpClient, private route: ActivatedRoute, private router: Router) {}

  submitForm() {
    if (this.selectedImages.length === 0) {
      return;
    }

    // Access query parameters
    this.listingId = this.route.snapshot.queryParamMap.get('listing') as string;
    console.log('Listing ID:', this.listingId);
    let params = new HttpParams().set('listing', this.listingId);

    const uploadRequests = [];

    for (const image of this.selectedImages) {
      const formData = new FormData();
      formData.append('image', image);

      const uploadRequest = this.http.post<any>('http://localhost:6077/realtor/uploads', formData, {params});
      uploadRequests.push(uploadRequest);
    }

    forkJoin(uploadRequests).subscribe(
      () => {
        console.log('Images uploaded successfully');
      },
      (error) => {
        console.error('Image upload failed:', error);
      }
    );
    setTimeout(() => {
      this.router.navigate(['/mylistings'])
    }, 5000)
  }

  onImagesSelected(event: any) {
    this.selectedImages = event.target.files;
  }
}
