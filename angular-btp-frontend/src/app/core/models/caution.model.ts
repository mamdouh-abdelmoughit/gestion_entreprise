export interface Caution {
  id: number;
  type: string;
  montant: number; // Corresponds to BigDecimal
  dateEmission: string; // Corresponds to LocalDateTime
  dateEcheance: string;
  beneficiaire: string;
  statut: string;
  projetId: number;
  fournisseurId: number;
  appelOffreId: number;
}

