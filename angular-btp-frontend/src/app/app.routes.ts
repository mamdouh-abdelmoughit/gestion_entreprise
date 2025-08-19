import { Routes } from '@angular/router';

// Layouts and Guards
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';
import { AuthGuard } from './core/guards/auth.guard';

// Page Components
import { LoginComponent } from './auth/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { ClientsComponent } from './components/clients/clients.component';
import { ClientFormComponent } from './components/clients/client-form/client-form.component';
import { ProjetsComponent } from './components/projets/projets.component';
import { ProjetFormComponent } from './components/projets/projet-form/projet-form.component';
import { AppelOffresComponent } from './components/appel-offres/appel-offres.component';
import { AppelOffreFormComponent } from './components/appel-offres/appel-offre-form/appel-offre-form.component';
import { CautionsComponent } from './components/cautions/cautions.component';
import { CautionFormComponent } from './components/cautions/caution-form/caution-form.component';
import { DecomptesComponent } from './components/decomptes/decomptes.component';
import { DecompteFormComponent } from './components/decomptes/decompte-form/decompte-form.component';
import { DepensesComponent } from './components/depenses/depenses.component';
import { DepenseFormComponent } from './components/depenses/depense-form/depense-form.component';
import { DocumentsComponent } from './components/documents/documents.component';
import { DocumentFormComponent } from './components/documents/document-form/document-form.component';
import { EmployesComponent } from './components/employes/employes.component';
import { EmployeFormComponent } from './components/employes/employe-form/employe-form.component';
import { FournisseursComponent } from './components/fournisseurs/fournisseurs.component';
import { FournisseurFormComponent } from './components/fournisseurs/fournisseur-form/fournisseur-form.component';
import { AffectationsListComponent } from './components/affectations/affectations-list/affectations-list.component';
import { AffectationFormComponent } from './components/affectations/affectation-form/affectation-form.component';
import { UsersListComponent } from './components/users/users-list/users-list.component';
import { UserFormComponent } from './components/users/user-form/user-form.component';
import { RolesListComponent } from './components/roles/roles-list/roles-list.component';
import { RegisterComponent } from './auth/register/register.component'; // 1. Import the component
import { LoginGuard } from './core/guards/login.guard'; // 1. Import the new LoginGuard




export const routes: Routes = [
  // --- PUBLIC ROUTES ---
  // These routes do not use the MainLayout and do not require the AuthGuard.
  { path: 'login', component: LoginComponent ,
    canActivate: [LoginGuard]},
  { path: 'register', component: RegisterComponent ,
    canActivate: [LoginGuard]}, // 2. Add the new route


  // --- AUTHENTICATED ROUTES ---
  // All routes inside this block will be children of the MainLayoutComponent.
  // The AuthGuard is applied ONCE to the parent, protecting all children.
  {
    path: '', // This acts as the parent route for the main application layout
    component: MainLayoutComponent,
    canActivate: [AuthGuard],
    children: [
      { path: 'dashboard', component: DashboardComponent },

      // Clients
      { path: 'clients', component: ClientsComponent },
      { path: 'clients/new', component: ClientFormComponent },
      { path: 'clients/edit/:id', component: ClientFormComponent },
      { path: 'clients/details/:id', component: ClientFormComponent },


      // Projets
      { path: 'projets', component: ProjetsComponent },
      { path: 'projets/new', component: ProjetFormComponent },
      { path: 'projets/edit/:id', component: ProjetFormComponent },
      { path: 'projets/details/:id', component: ProjetFormComponent },



      // Appel d'Offres
      { path: 'appel-offres', component: AppelOffresComponent },
      { path: 'appel-offres/new', component: AppelOffreFormComponent },
      { path: 'appel-offres/edit/:id', component: AppelOffreFormComponent },
      { path: 'appel-offres/details/:id', component: AppelOffreFormComponent },



      // Cautions
      { path: 'cautions', component: CautionsComponent },
      { path: 'cautions/new', component: CautionFormComponent },
      { path: 'cautions/edit/:id', component: CautionFormComponent },
      { path: 'cautions/details/:id', component: CautionFormComponent },





      // Décomptes
      { path: 'decomptes', component: DecomptesComponent },
      { path: 'decomptes/new', component: DecompteFormComponent },
      { path: 'decomptes/edit/:id', component: DecompteFormComponent },
      { path: 'decomptes/details/:id', component: DecompteFormComponent },



      // Dépenses
      { path: 'depenses', component: DepensesComponent },
      { path: 'depenses/new', component: DepenseFormComponent },
      { path: 'depenses/edit/:id', component: DepenseFormComponent },
      { path: 'depenses/details/:id', component: DepenseFormComponent },





      // Documents
      { path: 'documents', component: DocumentsComponent },
      { path: 'documents/new', component: DocumentFormComponent },
      { path: 'documents/edit/:id', component: DocumentFormComponent },
      { path: 'documents/details/:id', component: DocumentFormComponent },





      // Employés
      { path: 'employes', component: EmployesComponent },
      { path: 'employes/new', component: EmployeFormComponent },
      { path: 'employes/edit/:id', component: EmployeFormComponent },
      { path: 'employes/details/:id', component: EmployeFormComponent },



      // Affectations
      { path: 'affectations', component: AffectationsListComponent },
      { path: 'affectations/new', component: AffectationFormComponent },
      { path: 'affectations/edit/:id', component: AffectationFormComponent },
      { path: 'affectations/details/:id', component: AffectationFormComponent },



      // Fournisseurs
      { path: 'fournisseurs', component: FournisseursComponent },
      { path: 'fournisseurs/new', component: FournisseurFormComponent },
      { path: 'fournisseurs/edit/:id', component: FournisseurFormComponent },
      { path: 'fournisseurs/details/:id', component: FournisseurFormComponent },



      // User Management
      { path: 'users', component: UsersListComponent },
      { path: 'users/edit/:id', component: UserFormComponent },
      { path: 'users/details/:id', component: UserFormComponent },


      // Role Management
      { path: 'roles', component: RolesListComponent },

      // Default authenticated route
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },

  // --- WILDCARD ROUTE ---
  // This will catch any route that doesn't match the ones above.
  { path: '**', redirectTo: 'login', pathMatch: 'full' }
];
