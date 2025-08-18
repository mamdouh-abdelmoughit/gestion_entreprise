export interface AppelOffre {
  id: number;
  numero: string; // Add the property here
  titre: string;
  maitreDOuvrage: string;
  description: string;
  budgetEstimatif: number;
  datePublication: string;
  dateLimite: string;
  statut: string;
  projetId: number;
  fournisseurIds: number[];
}
