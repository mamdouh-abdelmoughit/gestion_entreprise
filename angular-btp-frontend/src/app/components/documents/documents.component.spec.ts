import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DocumentsComponent } from './documents.component';
import { DocumentService } from '../../core/services/document.service';
import { of, throwError } from 'rxjs';
import { By } from '@angular/platform-browser';

describe('DocumentsComponent', () => {
  let component: DocumentsComponent;
  let fixture: ComponentFixture<DocumentsComponent>;
  let documentService: jasmine.SpyObj<DocumentService>;

  const mockDocumentsPage = {
    content: [
      {
        id: 1,
        nom: 'Document A',
        type: 'CONTRAT',
        date: '2024-01-01'
      },
      {
        id: 2,
        nom: 'Document B',
        type: 'FACTURE',
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
    const documentServiceSpy = jasmine.createSpyObj('DocumentService', ['getAllDocuments']);

    await TestBed.configureTestingModule({
      imports: [DocumentsComponent],
      providers: [
        { provide: DocumentService, useValue: documentServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DocumentsComponent);
    component = fixture.componentInstance;
    documentService = TestBed.inject(DocumentService) as jasmine.SpyObj<DocumentService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display loading state initially', () => {
    documentService.getAllDocuments.and.returnValue(of(mockDocumentsPage));
    fixture.detectChanges();
    
    const loadingElement = fixture.debugElement.query(By.css('.loading-state'));
    expect(loadingElement).toBeTruthy();
  });

  it('should load and display documents', () => {
    documentService.getAllDocuments.and.returnValue(of(mockDocumentsPage));
    fixture.detectChanges();
    
    expect(component.documentsPage).toEqual(mockDocumentsPage);
    expect(component.isLoading).toBeFalse();
    
    const rows = fixture.debugElement.queryAll(By.css('tbody tr'));
    expect(rows.length).toBe(2);
  });

  it('should display error message when loading fails', () => {
    const errorMessage = 'Erreur lors du chargement des documents.';
    documentService.getAllDocuments.and.returnValue(throwError(() => new Error('Error')));
    
    fixture.detectChanges();
    
    expect(component.error).toBe(errorMessage);
    expect(component.isLoading).toBeFalse();
    
    const errorElement = fixture.debugElement.query(By.css('.error-message'));
    expect(errorElement.nativeElement.textContent).toContain(errorMessage);
  });

  it('should display correct document data in table', () => {
    documentService.getAllDocuments.and.returnValue(of(mockDocumentsPage));
    fixture.detectChanges();
    
    const rows = fixture.debugElement.queryAll(By.css('tbody tr'));
    const firstRow = rows[0];
    
    const cells = firstRow.queryAll(By.css('td'));
    expect(cells[0].nativeElement.textContent).toContain('Document A');
    expect(cells[1].nativeElement.textContent).toContain('CONTRAT');
  });

  it('should call loadDocuments on init', () => {
    const loadSpy = spyOn(component, 'loadDocuments');
    documentService.getAllDocuments.and.returnValue(of(mockDocumentsPage));
    
    component.ngOnInit();
    
    expect(loadSpy).toHaveBeenCalledWith(0, 10, 'date,desc');
  });
});
