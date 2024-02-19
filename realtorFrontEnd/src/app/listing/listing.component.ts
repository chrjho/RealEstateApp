import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-listing',
  templateUrl: './listing.component.html',
  styleUrls: ['./listing.component.css']
})
export class ListingComponent {
  @Input() address: string = '';
  @Input() pictureUrl: string = '';
  @Input() seller: string = '';
  @Output() listingClicked = new EventEmitter<string>();
  @Input() isSelected: boolean = false;

  onListingClick() {
    this.listingClicked.emit(this.address);
  }

  constructor() { }
}
