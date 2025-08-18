export interface Fournisseur {
  id: number;
  nom: string;
  contact: string; // Add the property here
  type: string; // Add the property here
  email: string;
  telephone: string;
  adresse: string;
  specialites: string[];
  statut: string;
}

