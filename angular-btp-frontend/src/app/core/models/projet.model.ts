export interface Projet {
  id: number;
  numero: string; // FIX: Add field
  nom: string;
  maitreDOuvrage: string; // FIX: Add field
  description: string;
  statut: string;
  dateDebut: string;
  dateFin: string;
  montantContrat: number; // FIX: Rename 'budget'
  adresse: string;
  clientId: number;
  chefProjetId: number;
}
