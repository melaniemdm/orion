export interface ArticleRequest {
    id: number;
  title: string;
  description: string;  // ✅ Correction (anciennement 'content')
  auteur_id: number;  // ✅ Ajouté pour récupérer l'auteur
  auteur_name?: string;  // ✅ Optionnel, car récupéré après
  created_date: string;  // ✅ Correction (anciennement 'date')
  theme_id?: number;

}
