export interface Employe {
  id: number;
  cin: string; // Add the property here
  nom: string;
  prenom: string;
  email: string;
  telephone: string;
  poste: string;
  dateEmbauche: string;
  salaire: number; // FIX: This property was missing
  adresse: string;
  statut: string;
}
