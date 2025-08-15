import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DecomptesComponent } from './decomptes.component';
import { DecompteService } from '../../core/services/decompte.service';
import { of, throwError } from 'rxjs';
import { By } from '@angular/platform-browser';

describe('DecomptesComponent', () => {
  let component: DecomptesComponent;
  let fixture: ComponentFixture<DecomptesComponent>;
  let decompteService: jasmine.SpyObj<DecompteService>;

  const mockDecomptesPage = {
    content: [
      {
        id: 1,
        nom: 'Decompte A',
        statut: 'EN_COURS',
        montant: 100000,
        date: '2024-01-01'
      },
      {
        id: 2,
        nom: 'Decompte B',
        statut: 'TERMINE',
        montant: 200000,
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
    const decompteServiceSpy = jasmine.createSpyObj('DecompteService', ['getAllDecomptes']);

    await TestBed.configureTestingModule({
      imports: [DecomptesComponent],
      providers: [
        { provide: DecompteService, useValue: decompteServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DecomptesComponent);
    component = fixture.componentInstance;
    decompteService = TestBed.inject(DecompteService) as jasmine.SpyObj<DecompteService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display loading state initially', () => {
    decompteService.getAllDecomptes.and.returnValue(of(mockDecomptesPage));
    fixture.detectChanges();
    
    const loadingElement = fixture.debugElement.query(By.css('.loading-state'));
    expect(loadingElement).toBeTruthy();
  });

  it('should load and display decomptes', () => {
    decompteService.getAllDecomptes.and.returnValue(of(mockDecomptesPage));
    fixture.detectChanges();
    
    expect(component.decomptesPage).toEqual(mockDecomptesPage);
    expect(component.isLoading).toBeFalse();
    
    const rows = fixture.debugElement.queryAll(By.css('tbody tr'));
    expect(rows.length).toBe(2);
  });

  it('should display error message when loading fails', () => {
    const errorMessage = 'Erreur lors du chargement des décomptes.';
    decompteService.getAllDecomptes.and.returnValue(throwError(() => new Error('Error')));
    
    fixture.detectChanges();
    
    expect(component.error).toBe(errorMessage);
    expect(component.isLoading).toBeFalse();
    
    const errorElement = fixture.debugElement.query(By.css('.error-message'));
    expect(errorElement.nativeElement.textContent).toContain(errorMessage);
  });

  it('should display correct decompte data in table', () => {
    decompteService.getAllDecomptes.and.returnValue(of(mockDecomptesPage));
    fixture.detectChanges();
    
    const rows = fixture.debugElement.queryAll(By.css('tbody tr'));
    const firstRow = rows[0];
    
    const cells = firstRow.queryAll(By.css('td'));
    expect(cells[0].nativeElement.textContent).toContain('Decompte A');
    expect(cells[1].query(By.css('.status-badge')).nativeElement.textContent).toContain('EN_COURS');
    expect(cells[3].nativeElement.textContent).toContain('€100,000.00');
  });

  it('should call loadDecomptes on init', () => {
    const loadSpy = spyOn(component, 'loadDecomptes');
    decompteService.getAllDecomptes.and.returnValue(of(mockDecomptesPage));
    
    component.ngOnInit();
    
    expect(loadSpy).toHaveBeenCalledWith(0, 10, 'dateCreation,desc');
  });
});
