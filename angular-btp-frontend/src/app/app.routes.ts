import { Routes } from '@angular/router';

// Auth Components
import { LoginComponent } from './auth/login/login.component';

// Main Layout Components
import { DashboardComponent } from './components/dashboard/dashboard.component';

// --- START OF IMPORTS ---

// Client Components
import { ClientsComponent } from './components/clients/clients.component';
import { ClientFormComponent } from './components/clients/client-form/client-form.component';

// Projet Components
import { ProjetsComponent } from './components/projets/projets.component';
import { ProjetFormComponent } from './components/projets/projet-form/projet-form.component';

// Appel d'Offres Components
import { AppelOffresComponent } from './components/appel-offres/appel-offres.component';
import { AppelOffreFormComponent } from './components/appel-offres/appel-offre-form/appel-offre-form.component';

// Caution Components
import { CautionsComponent } from './components/cautions/cautions.component';
import { CautionFormComponent } from './components/cautions/caution-form/caution-form.component';

// Decompte Components
import { DecomptesComponent } from './components/decomptes/decomptes.component';
import { DecompteFormComponent } from './components/decomptes/decompte-form/decompte-form.component';

// Depense Components
import { DepensesComponent } from './components/depenses/depenses.component';
import { DepenseFormComponent } from './components/depenses/depense-form/depense-form.component';

// Document Components
import { DocumentsComponent } from './components/documents/documents.component';
import { DocumentFormComponent } from './components/documents/document-form/document-form.component';

// Employe Components
import { EmployesComponent } from './components/employes/employes.component';
import { EmployeFormComponent } from './components/employes/employe-form/employe-form.component';

// Fournisseur Components
import { FournisseursComponent } from './components/fournisseurs/fournisseurs.component';
import { FournisseurFormComponent } from './components/fournisseurs/fournisseur-form/fournisseur-form.component';
import {AffectationsListComponent} from "./components/affectations/affectations-list/affectations-list.component";
import {AffectationFormComponent} from "./components/affectations/affectation-form/affectation-form.component";
import {UsersListComponent} from "./components/users/users-list/users-list.component";
import {UserFormComponent} from "./components/users/user-form/user-form.component";
import {RolesListComponent} from "./components/roles/roles-list/roles-list.component";

// --- END OF IMPORTS ---

export const routes: Routes = [
  // Authentication
  { path: 'login', component: LoginComponent },
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' }, // Redirect to dashboard after login as a default

  // Dashboard
  { path: 'dashboard', component: DashboardComponent },

  // Projets
  { path: 'projets', component: ProjetsComponent },
  { path: 'projets/new', component: ProjetFormComponent },
  { path: 'projets/edit/:id', component: ProjetFormComponent },

  // Clients
  { path: 'clients', component: ClientsComponent },
  { path: 'clients/new', component: ClientFormComponent },
  { path: 'clients/edit/:id', component: ClientFormComponent },

  // Appel d'Offres
  { path: 'appel-offres', component: AppelOffresComponent },
  { path: 'appel-offres/new', component: AppelOffreFormComponent },
  { path: 'appel-offres/edit/:id', component: AppelOffreFormComponent },

  // Cautions
  { path: 'cautions', component: CautionsComponent },
  { path: 'cautions/new', component: CautionFormComponent },
  { path: 'cautions/edit/:id', component: CautionFormComponent },

  // Décomptes
  { path: 'decomptes', component: DecomptesComponent },
  { path: 'decomptes/new', component: DecompteFormComponent },
  { path: 'decomptes/edit/:id', component: DecompteFormComponent },

  // Dépenses
  { path: 'depenses', component: DepensesComponent },
  { path: 'depenses/new', component: DepenseFormComponent },
  { path: 'depenses/edit/:id', component: DepenseFormComponent },

  // Documents
  { path: 'documents', component: DocumentsComponent },
  { path: 'documents/new', component: DocumentFormComponent },
  // Note: Document edit might not be a standard feature, but the route is here if you need it.
  { path: 'documents/edit/:id', component: DocumentFormComponent },

  // Employés
  { path: 'employes', component: EmployesComponent },
  { path: 'employes/new', component: EmployeFormComponent },
  { path: 'employes/edit/:id', component: EmployeFormComponent },

  // Affectations
  { path: 'affectations', component: AffectationsListComponent },
  { path: 'affectations/new', component: AffectationFormComponent },
  { path: 'affectations/edit/:id', component: AffectationFormComponent },

  // Fournisseurs
  { path: 'fournisseurs', component: FournisseursComponent },
  { path: 'fournisseurs/new', component: FournisseurFormComponent },
  { path: 'fournisseurs/edit/:id', component: FournisseurFormComponent },

  // User Management
  { path: 'users', component: UsersListComponent },
  { path: 'users/edit/:id', component: UserFormComponent },

  // Role Management
  { path: 'roles', component: RolesListComponent },

  // Wildcard route for 404 - Not Found pages
  // It's good practice to add this at the end
  { path: '**', redirectTo: '/dashboard' }
];
