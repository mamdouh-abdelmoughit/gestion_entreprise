import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EmployesComponent } from './employes.component';
import { EmployeService } from '../../core/services/employe.service';
import { of, throwError } from 'rxjs';
import { By } from '@angular/platform-browser';

describe('EmployesComponent', () => {
  let component: EmployesComponent;
  let fixture: ComponentFixture<EmployesComponent>;
  let employeService: jasmine.SpyObj<EmployeService>;

  const mockEmployesPage = {
    content: [
      {
        id: 1,
        nom: 'Dupont',
        prenom: 'Jean',
        email: 'jean.dupont@example.com',
        telephone: '0123456789'
      },
      {
        id: 2,
        nom: 'Martin',
        prenom: 'Marie',
        email: 'marie.martin@example.com',
        telephone: '0987654321'
      }
    ],
    totalElements: 2,
    totalPages: 1,
    size: 10,
    number: 0,
    pageable: {
      sort: { sorted: true, unsorted: false, empty: false },
      offset: 0,
      pageNumber: 0,
      pageSize: 10,
      paged: true,
      unpaged: false
    },
    last: true,
    first: true,
    numberOfElements: 2,
    empty: false
  };

  beforeEach(async () => {
    const employeServiceSpy = jasmine.createSpyObj('EmployeService', ['getAllEmployes']);

    await TestBed.configureTestingModule({
      imports: [EmployesComponent],
      providers: [
        { provide: EmployeService, useValue: employeServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(EmployesComponent);
    component = fixture.componentInstance;
    employeService = TestBed.inject(EmployeService) as jasmine.SpyObj<EmployeService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display loading state initially', () => {
    employeService.getAllEmployes.and.returnValue(of(mockEmployesPage));
    fixture.detectChanges();
    
    const loadingElement = fixture.debugElement.query(By.css('.loading-state'));
    expect(loadingElement).toBeTruthy();
  });

  it('should load and display employes', () => {
    employeService.getAllEmployes.and.returnValue(of(mockEmployesPage));
    fixture.detectChanges();
    
    expect(component.employesPage).toEqual(mockEmployesPage);
    expect(component.isLoading).toBeFalse();
    
    const rows = fixture.debugElement.queryAll(By.css('tbody tr'));
    expect(rows.length).toBe(2);
  });

  it('should display error message when loading fails', () => {
    const errorMessage = 'Erreur lors du chargement des employés.';
    employeService.getAllEmployes.and.returnValue(throwError(() => new Error('Error')));
    
    fixture.detectChanges();
    
    expect(component.error).toBe(errorMessage);
    expect(component.isLoading).toBeFalse();
    
    const errorElement = fixture.debugElement.query(By.css('.error-message'));
    expect(errorElement.nativeElement.textContent).toContain(errorMessage);
  });

  it('should display correct employe data in table', () => {
    employeService.getAllEmployes.and.returnValue(of(mockEmployesPage));
    fixture.detectChanges();
    
    const rows = fixture.debugElement.queryAll(By.css('tbody tr'));
    const firstRow = rows[0];
    
    const cells = firstRow.queryAll(By.css('td'));
    expect(cells[0].nativeElement.textContent).toContain('Dupont');
    expect(cells[1].nativeElement.textContent).toContain('Jean');
  });

  it('should call loadEmployes on init', () => {
    const loadSpy = spyOn(component, 'loadEmployes');
    employeService.getAllEmployes.and.returnValue(of(mockEmployesPage));
    
    component.ngOnInit();
    
    expect(loadSpy).toHaveBeenCalledWith(0, 10, 'nom,asc');
  });
});
