import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppelOffresComponent } from './appel-offres.component';
import { AppelOffreService } from '../../core/services/appel-offre.service';
import { of, throwError } from 'rxjs';
import { By } from '@angular/platform-browser';

describe('AppelOffresComponent', () => {
  let component: AppelOffresComponent;
  let fixture: ComponentFixture<AppelOffresComponent>;
  let appelOffreService: jasmine.SpyObj<AppelOffreService>;

  const mockAppelOffresPage = {
    content: [
      {
        id: 1,
        nom: 'Appel d\'offres A',
        statut: 'EN_COURS',
        dateDebut: '2024-01-01',
        budget: 100000
      },
      {
        id: 2,
        nom: 'Appel d\'offres B',
        statut: 'PLANIFIE',
        dateDebut: '2024-02-01',
        budget: 200000
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
    const appelOffreServiceSpy = jasmine.createSpyObj('AppelOffreService', ['getAllAppelOffres']);

    await TestBed.configureTestingModule({
      imports: [AppelOffresComponent],
      providers: [
        { provide: AppelOffreService, useValue: appelOffreServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AppelOffresComponent);
    component = fixture.componentInstance;
    appelOffreService = TestBed.inject(AppelOffreService) as jasmine.SpyObj<AppelOffreService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display loading state initially', () => {
    appelOffreService.getAllAppelOffres.and.returnValue(of(mockAppelOffresPage));
    fixture.detectChanges();
    
    const loadingElement = fixture.debugElement.query(By.css('.loading-state'));
    expect(loadingElement).toBeTruthy();
  });

  it('should load and display appel offres', () => {
    appelOffreService.getAllAppelOffres.and.returnValue(of(mockAppelOffresPage));
    fixture.detectChanges();
    
    expect(component.appelOffresPage).toEqual(mockAppelOffresPage);
    expect(component.isLoading).toBeFalse();
    
    const rows = fixture.debugElement.queryAll(By.css('tbody tr'));
    expect(rows.length).toBe(2);
  });

  it('should display error message when loading fails', () => {
    const errorMessage = 'Erreur lors du chargement des appels d\'offres.';
    appelOffreService.getAllAppelOffres.and.returnValue(throwError(() => new Error('Error')));
    
    fixture.detectChanges();
    
    expect(component.error).toBe(errorMessage);
    expect(component.isLoading).toBeFalse();
    
    const errorElement = fixture.debugElement.query(By.css('.error-message'));
    expect(errorElement.nativeElement.textContent).toContain(errorMessage);
  });

  it('should display correct appel offre data in table', () => {
    appelOffreService.getAllAppelOffres.and.returnValue(of(mockAppelOffresPage));
    fixture.detectChanges();
    
    const rows = fixture.debugElement.queryAll(By.css('tbody tr'));
    const firstRow = rows[0];
    
    const cells = firstRow.queryAll(By.css('td'));
    expect(cells[0].nativeElement.textContent).toContain('Appel d\'offres A');
    expect(cells[1].query(By.css('.status-badge')).nativeElement.textContent).toContain('EN_COURS');
    expect(cells[3].nativeElement.textContent).toContain('€100,000.00');
  });

  it('should call loadAppelOffres on init', () => {
    const loadSpy = spyOn(component, 'loadAppelOffres');
    appelOffreService.getAllAppelOffres.and.returnValue(of(mockAppelOffresPage));
    
    component.ngOnInit();
    
    expect(loadSpy).toHaveBeenCalledWith(0, 10, 'dateDebut,desc');
  });

  it('should display pagination info', () => {
    appelOffreService.getAllAppelOffres.and.returnValue(of(mockAppelOffresPage));
    fixture.detectChanges();
    
    const paginationInfo = fixture.debugElement.query(By.css('.pagination-info'));
    expect(paginationInfo).toBeTruthy();
    expect(paginationInfo.nativeElement.textContent).toContain('Affichage de 1 à 2 sur 2 appels d\'offres');
  });
});
