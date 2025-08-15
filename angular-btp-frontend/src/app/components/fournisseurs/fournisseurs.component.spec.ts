import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FournisseursComponent } from './fournisseurs.component';
import { FournisseurService } from '../../core/services/fournisseur.service';
import { of, throwError } from 'rxjs';
import { By } from '@angular/platform-browser';

describe('FournisseursComponent', () => {
  let component: FournisseursComponent;
  let fixture: ComponentFixture<FournisseursComponent>;
  let fournisseurService: jasmine.SpyObj<FournisseurService>;

  const mockFournisseursPage = {
    content: [
      {
        id: 1,
        nom: 'Fournisseur A',
        email: 'contact@fournisseur-a.com',
        telephone: '0123456789',
        statut: 'ACTIF'
      },
      {
        id: 2,
        nom: 'Fournisseur B',
        email: 'contact@fournisseur-b.com',
        telephone: '0987654321',
        statut: 'INACTIF'
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
    const fournisseurServiceSpy = jasmine.createSpyObj('FournisseurService', ['getAllFournisseurs']);

    await TestBed.configureTestingModule({
      imports: [FournisseursComponent],
      providers: [
        { provide: FournisseurService, useValue: fournisseurServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(FournisseursComponent);
    component = fixture.componentInstance;
    fournisseurService = TestBed.inject(FournisseurService) as jasmine.SpyObj<FournisseurService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display loading state initially', () => {
    fournisseurService.getAllFournisseurs.and.returnValue(of(mockFournisseursPage));
    fixture.detectChanges();
    
    const loadingElement = fixture.debugElement.query(By.css('.loading-state'));
    expect(loadingElement).toBeTruthy();
  });

  it('should load and display fournisseurs', () => {
    fournisseurService.getAllFournisseurs.and.returnValue(of(mockFournisseursPage));
    fixture.detectChanges();
    
    expect(component.fournisseursPage).toEqual(mockFournisseursPage);
    expect(component.isLoading).toBeFalse();
    
    const rows = fixture.debugElement.queryAll(By.css('tbody tr'));
    expect(rows.length).toBe(2);
  });

  it('should display error message when loading fails', () => {
    const errorMessage = 'Erreur lors du chargement des fournisseurs.';
    fournisseurService.getAllFournisseurs.and.returnValue(throwError(() => new Error('Error')));
    
    fixture.detectChanges();
    
    expect(component.error).toBe(errorMessage);
    expect(component.isLoading).toBeFalse();
    
    const errorElement = fixture.debugElement.query(By.css('.error-message'));
    expect(errorElement.nativeElement.textContent).toContain(errorMessage);
  });

  it('should display correct fournisseur data in table', () => {
    fournisseurService.getAllFournisseurs.and.returnValue(of(mockFournisseursPage));
    fixture.detectChanges();
    
    const rows = fixture.debugElement.queryAll(By.css('tbody tr'));
    const firstRow = rows[0];
    
    const cells = firstRow.queryAll(By.css('td'));
    expect(cells[0].nativeElement.textContent).toContain('Fournisseur A');
    expect(cells[1].nativeElement.textContent).toContain('contact@fournisseur-a.com');
  });

  it('should call loadFournisseurs on init', () => {
    const loadSpy = spyOn(component, 'loadFournisseurs');
    fournisseurService.getAllFournisseurs.and.returnValue(of(mockFournisseursPage));
    
    component.ngOnInit();
    
    expect(loadSpy).toHaveBeenCalledWith(0, 10, 'nom,asc');
  });
});
