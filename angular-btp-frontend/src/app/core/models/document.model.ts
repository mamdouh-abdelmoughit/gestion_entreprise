export interface Document {
  id: number;
  nom: string;
  type: string;
  chemin: string;
  dateUpload: string; // Corresponds to LocalDateTime
  taille: number;
  description: string;
  projetId: number;
  employeId: number;
}

