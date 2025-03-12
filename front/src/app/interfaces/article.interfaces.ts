export interface ArticleRequest {
  id: string;
  theme: string;
  title: string;
  auteur_id?: number;
  description: string;
  theme_id?: number;
created_date: string;
}
export interface ArticleResponse {
  post: ArticleRequest[];
}
export interface SingleArticleResponse {
  post: ArticleRequest; // un seul article
}
