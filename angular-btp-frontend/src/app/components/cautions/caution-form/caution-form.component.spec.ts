import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CautionFormComponent } from './caution-form.component';

describe('CautionFormComponent', () => {
  let component: CautionFormComponent;
  let fixture: ComponentFixture<CautionFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CautionFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CautionFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
