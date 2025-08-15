import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppelOffreFormComponent } from './appel-offre-form.component';

describe('AppelOffreFormComponent', () => {
  let component: AppelOffreFormComponent;
  let fixture: ComponentFixture<AppelOffreFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppelOffreFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AppelOffreFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
