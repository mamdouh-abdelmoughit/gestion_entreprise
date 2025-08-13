export interface Projet {
  id: number;
  nom: string;
  description: string;
  statut: string;
  dateDebut: string; // Corresponds to LocalDate
  dateFin: string;   // Corresponds to LocalDate
  budget: number;    // Corresponds to BigDecimal
  adresse: string;
  clientId: number;
  responsableId: number;
}

