import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DiscountedproductsComponent } from './discountedproducts.component';

describe('DiscountedproductsComponent', () => {
  let component: DiscountedproductsComponent;
  let fixture: ComponentFixture<DiscountedproductsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DiscountedproductsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DiscountedproductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
