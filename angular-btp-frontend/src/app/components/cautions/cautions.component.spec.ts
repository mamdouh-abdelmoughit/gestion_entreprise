import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CautionsComponent } from './cautions.component';
import { CautionService } from '../../core/services/caution.service';
import { of, throwError } from 'rxjs';
import { By } from '@angular/platform-browser';

describe('CautionsComponent', () => {
  let component: CautionsComponent;
  let fixture: ComponentFixture<CautionsComponent>;
  let cautionService: jasmine.SpyObj<CautionService>;

  const mockCautionsPage = {
    content: [
      {
        id: 1,
        nom: 'Caution A',
        type: 'BANCAIRE',
        montant: 50000,
        statut: 'ACTIF',
        dateEmission: '2024-01-01',
        dateEcheance: '2024-12-31',
        beneficiaire: 'Client A',
        projetId: 1,
        // Add other required properties here
      },
      {
        id: 2,
        nom: 'Caution B',
        type: 'GARANTIE',
        montant: 75000,
        statut: 'INACTIF',
        dateEmission: '2024-01-01',
        dateEcheance: '2024-12-31',
        beneficiaire: 'Client B',
        projetId: 2,
        // Add other required properties here
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
    const cautionServiceSpy = jasmine.createSpyObj('CautionService', ['getAllCautions']);

    await TestBed.configureTestingModule({
      imports: [CautionsComponent],
      providers: [
        { provide: CautionService, useValue: cautionServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CautionsComponent);
    component = fixture.componentInstance;
    cautionService = TestBed.inject(CautionService) as jasmine.SpyObj<CautionService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display loading state initially', () => {
    cautionService.getAllCautions.and.returnValue(of(mockCautionsPage));
    fixture.detectChanges();
    
    const loadingElement = fixture.debugElement.query(By.css('.loading-state'));
    expect(loadingElement).toBeTruthy();
  });

  it('should load and display cautions', () => {
    cautionService.getAllCautions.and.returnValue(of(mockCautionsPage));
    fixture.detectChanges();
    
    expect(component.cautionsPage).toEqual(mockCautionsPage);
    expect(component.isLoading).toBeFalse();
    
    const rows = fixture.debugElement.queryAll(By.css('tbody tr'));
    expect(rows.length).toBe(2);
  });

  it('should display error message when loading fails', () => {
    const errorMessage = 'Erreur lors du chargement des cautions.';
    cautionService.getAllCautions.and.returnValue(throwError(() => new Error('Error')));
    
    fixture.detectChanges();
    
    expect(component.error).toBe(errorMessage);
    expect(component.isLoading).toBeFalse();
    
    const errorElement = fixture.debugElement.query(By.css('.error-message'));
    expect(errorElement.nativeElement.textContent).toContain(errorMessage);
  });

  it('should display correct caution data in table', () => {
    cautionService.getAllCautions.and.returnValue(of(mockCautionsPage));
    fixture.detectChanges();
    
    const rows = fixture.debugElement.queryAll(By.css('tbody tr'));
    const firstRow = rows[0];
    
    const cells = firstRow.queryAll(By.css('td'));
    expect(cells[0].nativeElement.textContent).toContain('Caution A');
    expect(cells[1].nativeElement.textContent).toContain('BANCAIRE');
    expect(cells[2].nativeElement.textContent).toContain('€50,000.00');
  });

  it('should call loadCautions on init', () => {
    const loadSpy = spyOn(component, 'loadCautions');
    cautionService.getAllCautions.and.returnValue(of(mockCautionsPage));
    
    component.ngOnInit();
    
    expect(loadSpy).toHaveBeenCalledWith(0, 10, 'dateCreation,desc');
  });
});
