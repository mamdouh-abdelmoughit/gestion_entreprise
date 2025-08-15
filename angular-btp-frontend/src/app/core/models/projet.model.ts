export interface Projet {
  id: number;
  nom: string;
  description: string;
  statut: string;
  dateDebut: string;
  dateFin: string;
  budget: number;
  adresse: string;
  clientId: number;
  chefProjetId: number; // FIX: Was 'responsableId'
}
