export interface Document {
  id: number;
  nom: string;
  type: string;
  fichier: string;
  dateUpload: string; // Corresponds to LocalDateTime
  taille: number;
  description: string;
  projetId: number;
  employeId: number;
  appelOffreId: number; // Add the new property here
}

