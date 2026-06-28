import { Routes } from '@angular/router';
import { ActivateAccountComponent } from './components/activate-account.component';
import { ResetPasswordComponent } from './components/reset-password.component';

// Layouts and Guards
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';
import { AuthGuard } from './core/guards/auth.guard';
import { RoleGuard } from './core/guards/role.guard';

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
import { AttachementsComponent } from './components/attachements/attachements.component';
import { AttachementFormComponent } from './components/attachements/attachement-form/attachement-form.component';
import { FacturesComponent } from './components/factures/factures.component';
import { FactureFormComponent } from './components/factures/facture-form/facture-form.component';
import { PaiementsComponent } from './components/paiements/paiements.component';
import { ChequesEffetsComponent } from './components/cheques-effets/cheques-effets.component';
import { MessagerieComponent } from './components/messagerie/messagerie.component';
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
import { RegisterComponent } from './auth/register/register.component';
import { LoginGuard } from './core/guards/login.guard';

// Role constants for clarity
const ADMIN = 'ROLE_ADMIN';
const EMPLOYEE = 'ROLE_EMPLOYEE';
const CP = 'ROLE_CP';
const CC = 'ROLE_CC';
const CLIENT = 'ROLE_CLIENT';
const FOURNISSEUR = 'ROLE_FOURNISSEUR';




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
      // Dashboard - accessible to all authenticated users
      { path: 'dashboard', component: DashboardComponent },

      // Clients - ADMIN only
      { path: 'clients', component: ClientsComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'clients/new', component: ClientFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'clients/edit/:id', component: ClientFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'clients/details/:id', component: ClientFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },

      // Projets - ADMIN, EMPLOYEE, CLIENT
      { path: 'projets', component: ProjetsComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, EMPLOYEE, CLIENT] } },
      { path: 'projets/new', component: ProjetFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'projets/edit/:id', component: ProjetFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'projets/details/:id', component: ProjetFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, EMPLOYEE, CLIENT] } },

      // Appel d'Offres - ADMIN, FOURNISSEUR
      { path: 'appel-offres', component: AppelOffresComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, FOURNISSEUR] } },
      { path: 'appel-offres/new', component: AppelOffreFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'appel-offres/edit/:id', component: AppelOffreFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'appel-offres/details/:id', component: AppelOffreFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, FOURNISSEUR] } },

      // Cautions - ADMIN, FOURNISSEUR
      { path: 'cautions', component: CautionsComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, FOURNISSEUR] } },
      { path: 'cautions/new', component: CautionFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'cautions/edit/:id', component: CautionFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'cautions/details/:id', component: CautionFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, FOURNISSEUR] } },

      // Décomptes - ADMIN, CLIENT
      { path: 'decomptes', component: DecomptesComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, CLIENT] } },
      { path: 'decomptes/new', component: DecompteFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'decomptes/edit/:id', component: DecompteFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'decomptes/details/:id', component: DecompteFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, CLIENT] } },

      // Attachements - ADMIN, CP, CC, CLIENT (replaces Décomptes)
      { path: 'attachements', component: AttachementsComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, CP, CC, CLIENT] } },
      { path: 'attachements/new', component: AttachementFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, CP, CC] } },
      { path: 'attachements/edit/:id', component: AttachementFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, CP, CC] } },

      // Factures - ADMIN, CP
      { path: 'factures', component: FacturesComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, CP] } },
      { path: 'factures/new', component: FactureFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, CP] } },
      { path: 'factures/edit/:id', component: FactureFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, CP] } },

      // Paiements - ADMIN, CP
      { path: 'paiements', component: PaiementsComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, CP] } },

      // Chèques & Effets - ADMIN, CP
      { path: 'cheques-effets', component: ChequesEffetsComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, CP] } },

      // Messagerie - all project participants
      { path: 'messagerie', component: MessagerieComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, CP, CC, CLIENT] } },

      // Dépenses - ADMIN, EMPLOYEE
      { path: 'depenses', component: DepensesComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, EMPLOYEE] } },
      { path: 'depenses/new', component: DepenseFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, EMPLOYEE] } },
      { path: 'depenses/edit/:id', component: DepenseFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'depenses/details/:id', component: DepenseFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, EMPLOYEE] } },

      // Documents - All authenticated users
      { path: 'documents', component: DocumentsComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, EMPLOYEE, CLIENT, FOURNISSEUR] } },
      { path: 'documents/new', component: DocumentFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, EMPLOYEE] } },
      { path: 'documents/edit/:id', component: DocumentFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'documents/details/:id', component: DocumentFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, EMPLOYEE, CLIENT, FOURNISSEUR] } },

      // Employés - ADMIN only
      { path: 'employes', component: EmployesComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'employes/new', component: EmployeFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'employes/edit/:id', component: EmployeFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'employes/details/:id', component: EmployeFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },

      // Affectations - ADMIN, EMPLOYEE
      { path: 'affectations', component: AffectationsListComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, EMPLOYEE] } },
      { path: 'affectations/new', component: AffectationFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'affectations/edit/:id', component: AffectationFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'affectations/details/:id', component: AffectationFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN, EMPLOYEE] } },

      // Fournisseurs - ADMIN only
      { path: 'fournisseurs', component: FournisseursComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'fournisseurs/new', component: FournisseurFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'fournisseurs/edit/:id', component: FournisseurFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'fournisseurs/details/:id', component: FournisseurFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },

      // User Management - ADMIN only
      { path: 'users', component: UsersListComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'users/edit/:id', component: UserFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'users/details/:id', component: UserFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },
      { path: 'users/new', component: UserFormComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },

      // Role Management - ADMIN only
      { path: 'roles', component: RolesListComponent, canActivate: [RoleGuard], data: { roles: [ADMIN] } },

      // Default authenticated route
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },

  // Public routes for activation/reset
  { path: 'activate', component: ActivateAccountComponent },
  { path: 'reset-password', component: ResetPasswordComponent },

  // --- WILDCARD ROUTE ---
  // This will catch any route that doesn't match the ones above.
  { path: '**', redirectTo: '', pathMatch: 'full' }
];
