import { Component, OnInit } from '@angular/core';
import { ArticleRequest } from 'src/app/interfaces/article.interfaces';
import { ArticleService } from 'src/app/services/article.service';

@Component({
  selector: 'app-themes',
  templateUrl: './themes.component.html',
  styleUrls: ['./themes.component.scss']
})
export class ThemesComponent implements OnInit {
  articles: ArticleRequest[] = [];
  public themes: any[] = [];
  
  constructor(private articleService: ArticleService) { }

  ngOnInit(): void {
    this.articleService.getAllArticles()
      .subscribe({
        next: (data: ArticleRequest[]) => {
          this.articles = data; // On récupère la liste d’articles
        },
        error: (err) => {
          console.error('Erreur lors de la récupération des articles :', err);
        }
      });
      this.loadThemes();
  }
  private loadThemes(): void {
    this.articleService.getThemes().subscribe({
      next: (response) => {
        // response.subject doit contenir le tableau des thèmes
        if (response && Array.isArray(response.subject)) {
          this.themes = response.subject;
        } else {
          console.error("Erreur : la réponse des thèmes n'est pas un tableau", response);
          this.themes = [];
        }
        // Une fois les thèmes chargés, on peut récupérer les articles
        this.loadArticles();
      },
      error: (err) => {
        console.error("Erreur lors du chargement des thèmes :", err);
      }
    });
  }

  private loadArticles(): void {
    this.articleService.getAllArticles().subscribe({
      next: (data: ArticleRequest[]) => {
        // Associe à chaque article le nom du thème correspondant
        this.articles = data.map(article => {
          const foundTheme = this.themes.find(t => t.id === article.theme_id);
          return {
            ...article,
            // On ajoute un champ themeName (ou tout autre nom) qu’on affichera plus tard
            themeName: foundTheme ? foundTheme.name_theme : 'Thème inconnu'
          };
        });
      },
      error: (err) => {
        console.error('Erreur lors de la récupération des articles :', err);
      }
    });
  }


}
