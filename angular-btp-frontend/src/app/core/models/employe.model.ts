export interface Employe {
  id: number;
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
