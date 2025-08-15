export interface Decompte {
  id: number;
  numero: string; // FIX: This is the correct property, not 'nom'
  dateDecompte: string; // FIX: This is the correct property, not 'date'
  montantTotal: number; // FIX: This is the correct property, not 'montant'
  montantPaye: number;
  montantRestant: number;
  description: string;
  statut: string;
  projetId: number;
}
