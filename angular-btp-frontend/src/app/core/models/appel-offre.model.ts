export interface AppelOffre {
  id: number;
  titre: string;
  description: string;
  budgetEstimatif: number; // Corresponds to BigDecimal
  datePublication: string; // Corresponds to LocalDateTime
  dateLimite: string;
  statut: string;
  projetId: number;
  fournisseurIds: number[];
}

