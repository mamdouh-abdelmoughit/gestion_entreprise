export interface AppelOffre {
  id: number;
  titre: string; // FIX: This is the correct property name, not 'nom'
  description: string;
  budgetEstimatif: number;
  datePublication: string;
  dateLimite: string;
  statut: string;
  projetId: number;
  fournisseurIds: number[];
}
