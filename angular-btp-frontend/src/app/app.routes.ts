import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { AppelOffresComponent } from './components/appel-offres/appel-offres.component';
import { ProjetsComponent } from './components/projets/projets.component';
import { CautionsComponent } from './components/cautions/cautions.component';
import { DecomptesComponent } from './components/decomptes/decomptes.component';
import { DocumentsComponent } from './components/documents/documents.component';
import { EmployesComponent } from './components/employes/employes.component';
import { FournisseursComponent } from './components/fournisseurs/fournisseurs.component';
import { DepensesComponent } from './components/depenses/depenses.component';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'appel-offres', component: AppelOffresComponent },
  { path: 'projets', component: ProjetsComponent },
  { path: 'cautions', component: CautionsComponent },
  { path: 'decomptes', component: DecomptesComponent },
  { path: 'documents', component: DocumentsComponent },
  { path: 'employes', component: EmployesComponent },
  { path: 'fournisseurs', component: FournisseursComponent },
  { path: 'depenses', component: DepensesComponent }
];
