export interface AffectationEmploye {
  id: number;
  employeId: number;
  projetId: number;
  role: string;
  dateDebut: string; // ISO Date String
  dateFin?: string; // Optional
  statut: string;
// Optional: For display purposes, you might want to add these later
// employeNom?: string;
// projetNom?: string;
}
