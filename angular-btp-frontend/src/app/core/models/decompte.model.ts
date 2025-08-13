export interface Decompte {
  id: number;
  numero: string;
  dateDecompte: string; // Corresponds to LocalDateTime
  montantTotal: number; // Corresponds to BigDecimal
  montantPaye: number;
  montantRestant: number;
  description: string;
  statut: string;
  projetId: number;
}

