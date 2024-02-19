import { HttpClient } from '@angular/common/http';
import { Component, ViewChild, ElementRef, AfterViewInit, Input } from '@angular/core';
import { HouseData } from '../house-data.interface';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements AfterViewInit {
  @ViewChild('mapIframe', { static: false }) mapIframe!: ElementRef;
  @Input() houseData: any;
  @Input() selectedListing: string | null = null;

  ngAfterViewInit() {
    this.mapIframe.nativeElement.addEventListener('load', () => {
      //   // Send data to the iframe
        console.log('Map Component got data');
        console.log(this.houseData);
        this.mapIframe.nativeElement.contentWindow.postMessage(this.houseData, '*');
      });
  }
  onListingClick(address: string) {
    // Get the iframe element
    const iframe = document.getElementById('mapIframe') as HTMLIFrameElement;
  
    // Send a message to the iframe's content window
    iframe.contentWindow?.postMessage({ action: 'openMarkerInfoWindow', address }, '*');
  }

  
}