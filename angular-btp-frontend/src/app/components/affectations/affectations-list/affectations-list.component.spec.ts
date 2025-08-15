import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AffectationsListComponent } from './affectations-list.component';

describe('AffectationsListComponent', () => {
  let component: AffectationsListComponent;
  let fixture: ComponentFixture<AffectationsListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AffectationsListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AffectationsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
