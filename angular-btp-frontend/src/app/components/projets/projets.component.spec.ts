import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProjetsComponent } from './projets.component';
import { ProjetService } from '../../core/services/projet.service';
import { of, throwError } from 'rxjs';
import { By } from '@angular/platform-browser';

describe('ProjetsComponent', () => {
  let component: ProjetsComponent;
  let fixture: ComponentFixture<ProjetsComponent>;
  let projetService: jasmine.SpyObj<ProjetService>;

  const mockProjetsPage = {
    content: [
      {
        id: 1,
        nom: 'Projet A',
        statut: 'EN_COURS',
        dateDebut: '2024-01-01',
        budget: 100000
      },
      {
        id: 2,
        nom: 'Projet B',
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
    const projetServiceSpy = jasmine.createSpyObj('ProjetService', ['getAllProjets']);

    await TestBed.configureTestingModule({
      imports: [ProjetsComponent],
      providers: [
        { provide: ProjetService, useValue: projetServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ProjetsComponent);
    component = fixture.componentInstance;
    projetService = TestBed.inject(ProjetService) as jasmine.SpyObj<ProjetService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display loading state initially', () => {
    projetService.getAllProjets.and.returnValue(of(mockProjetsPage));
    fixture.detectChanges();
    
    const loadingElement = fixture.debugElement.query(By.css('.loading-state'));
    expect(loadingElement).toBeTruthy();
  });

  it('should load and display projects', () => {
    projetService.getAllProjets.and.returnValue(of(mockProjetsPage));
    fixture.detectChanges();
    
    expect(component.projetsPage).toEqual(mockProjetsPage);
    expect(component.isLoading).toBeFalse();
    
    const rows = fixture.debugElement.queryAll(By.css('tbody tr'));
    expect(rows.length).toBe(2);
  });

  it('should display error message when loading fails', () => {
    const errorMessage = 'Erreur lors du chargement des projets.';
    projetService.getAllProjets.and.returnValue(throwError(() => new Error('Error')));
    
    fixture.detectChanges();
    
    expect(component.error).toBe(errorMessage);
    expect(component.isLoading).toBeFalse();
    
    const errorElement = fixture.debugElement.query(By.css('.error-message'));
    expect(errorElement.nativeElement.textContent).toContain(errorMessage);
  });

  it('should display correct project data in table', () => {
    projetService.getAllProjets.and.returnValue(of(mockProjetsPage));
    fixture.detectChanges();
    
    const rows = fixture.debugElement.queryAll(By.css('tbody tr'));
    const firstRow = rows[0];
    
    const cells = firstRow.queryAll(By.css('td'));
    expect(cells[0].nativeElement.textContent).toContain('Projet A');
    expect(cells[1].query(By.css('.status-badge')).nativeElement.textContent).toContain('EN_COURS');
    expect(cells[3].nativeElement.textContent).toContain('€100,000.00');
  });

  it('should call loadProjets on init', () => {
    const loadSpy = spyOn(component, 'loadProjets');
    projetService.getAllProjets.and.returnValue(of(mockProjetsPage));
    
    component.ngOnInit();
    
    expect(loadSpy).toHaveBeenCalledWith(0, 10, 'dateDebut,desc');
  });

  it('should display pagination info', () => {
    projetService.getAllProjets.and.returnValue(of(mockProjetsPage));
    fixture.detectChanges();
    
    const paginationInfo = fixture.debugElement.query(By.css('.pagination-info'));
    expect(paginationInfo).toBeTruthy();
    expect(paginationInfo.nativeElement.textContent).toContain('Affichage de 1 à 2 sur 2 projets');
  });
});
