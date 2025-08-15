import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DashboardComponent } from './dashboard.component';
import { By } from '@angular/platform-browser';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display the dashboard title', () => {
    const titleElement = fixture.debugElement.query(By.css('.dashboard-title'));
    expect(titleElement.nativeElement.textContent).toContain('Tableau de bord');
  });

  it('should display 4 stat cards', () => {
    const statCards = fixture.debugElement.queryAll(By.css('.stat-card'));
    expect(statCards.length).toBe(4);
  });

  it('should display correct stat titles', () => {
    const statTitles = fixture.debugElement.queryAll(By.css('.stat-title'));
    const expectedTitles = ['Projets en cours', 'Appels d\'offres', 'Budget total', 'Employés'];
    
    statTitles.forEach((title, index) => {
      expect(title.nativeElement.textContent).toContain(expectedTitles[index]);
    });
  });

  it('should display activity section', () => {
    const activitySection = fixture.debugElement.query(By.css('.activity-section'));
    expect(activitySection).toBeTruthy();
  });

  it('should display stats section', () => {
    const statsSection = fixture.debugElement.query(By.css('.stats-section'));
    expect(statsSection).toBeTruthy();
  });

  it('should have responsive layout classes', () => {
    const statsGrid = fixture.debugElement.query(By.css('.stats-grid'));
    expect(statsGrid.nativeElement.classList.contains('md:grid-cols-2')).toBeTruthy();
    expect(statsGrid.nativeElement.classList.contains('lg:grid-cols-4')).toBeTruthy();
  });
});
