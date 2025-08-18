export interface Caution {
  id: number;
  numero: string;
  type: string;
  banque: string; // Add the property here
  montant: number;
  dateEmission: string;
  dateEcheance: string;
  beneficiaire: string;
  statut: string;
  projetId: number;
  fournisseurId: number;
  appelOffreId: number;
}
