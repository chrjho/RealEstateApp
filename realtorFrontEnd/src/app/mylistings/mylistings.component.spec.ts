import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MylistingsComponent } from './mylistings.component';

describe('MylistingsComponent', () => {
  let component: MylistingsComponent;
  let fixture: ComponentFixture<MylistingsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MylistingsComponent]
    });
    fixture = TestBed.createComponent(MylistingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
