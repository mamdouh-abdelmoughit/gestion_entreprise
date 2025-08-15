import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DepensesComponent } from './depenses.component';
import { DepenseService } from '../../core/services/depense.service';
import { of, throwError } from 'rxjs';
import { By } from '@angular/platform-browser';

describe('DepensesComponent', () => {
  let component: DepensesComponent;
  let fixture: ComponentFixture<DepensesComponent>;
  let depenseService: jasmine.SpyObj<DepenseService>;

  const mockDepensesPage = {
    content: [
      {
        id: 1,
        description: 'Dépense A',
        montant: 100000,
        categorie: 'Fournitures',
        date: '2024-01-01'
      },
      {
        id: 2,
        description: 'Dépense B',
        montant: 200000,
        categorie: 'Services',
        date: '2024-02-01'
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
    const depenseServiceSpy = jasmine.createSpyObj('DepenseService', ['getAllDepenses']);

    await TestBed.configureTestingModule({
      imports: [DepensesComponent],
      providers: [
        { provide: DepenseService, useValue: depenseServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DepensesComponent);
    component = fixture.componentInstance;
    depenseService = TestBed.inject(DepenseService) as jasmine.SpyObj<DepenseService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display loading state initially', () => {
    depenseService.getAllDepenses.and.returnValue(of(mockDepensesPage));
    fixture.detectChanges();
    
    const loadingElement = fixture.debugElement.query(By.css('.loading-state'));
    expect(loadingElement).toBeTruthy();
  });

  it('should load and display depenses', () => {
    depenseService.getAllDepenses.and.returnValue(of(mockDepensesPage));
    fixture.detectChanges();
    
    expect(component.depensesPage).toEqual(mockDepensesPage);
    expect(component.isLoading).toBeFalse();
    
    const rows = fixture.debugElement.queryAll(By.css('tbody tr'));
    expect(rows.length).toBe(2);
  });

  it('should display error message when loading fails', () => {
    const errorMessage = 'Erreur lors du chargement des dépenses.';
    depenseService.getAllDepenses.and.returnValue(throwError(() => new Error('Error')));
    
    fixture.detectChanges();
    
    expect(component.error).toBe(errorMessage);
    expect(component.isLoading).toBeFalse();
    
    const errorElement = fixture.debugElement.query(By.css('.error-message'));
    expect(errorElement.nativeElement.textContent).toContain(errorMessage);
  });

  it('should display correct depense data in table', () => {
    depenseService.getAllDepenses.and.returnValue(of(mockDepensesPage));
    fixture.detectChanges();
    
    const rows = fixture.debugElement.queryAll(By.css('tbody tr'));
    const firstRow = rows[0];
    
    const cells = firstRow.queryAll(By.css('td'));
    expect(cells[0].nativeElement.textContent).toContain('Dépense A');
    expect(cells[1].nativeElement.textContent).toContain('€100,000.00');
  });

  it('should call loadDepenses on init', () => {
    const loadSpy = spyOn(component, 'loadDepenses');
    depenseService.getAllDepenses.and.returnValue(of(mockDepensesPage));
    
    component.ngOnInit();
    
    expect(loadSpy).toHaveBeenCalledWith(0, 10, 'date,desc');
  });
});
