export interface Caution {
  id: number;
  numero: string; // FIX: This property was missing
  type: string;
  montant: number;
  dateEmission: string;
  dateEcheance: string;
  beneficiaire: string;
  statut: string;
  projetId: number;
  fournisseurId: number;
  appelOffreId: number;
}
