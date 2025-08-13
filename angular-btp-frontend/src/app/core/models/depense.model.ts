export interface Depense {
  id: number;
  description: string;
  montant: number; // Corresponds to BigDecimal
  dateDepense: string; // Corresponds to LocalDateTime
  categorie: string;
  statut: string;
  projetId: number;
  employeId: number;
}

