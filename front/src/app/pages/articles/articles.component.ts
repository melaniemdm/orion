import { Component, OnInit } from '@angular/core';
import { ArticleRequest } from 'src/app/interfaces/article.interfaces';
import { ArticleService } from 'src/app/services/article.service';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.scss']
})
export class ArticlesComponent implements OnInit {

// On stocke la liste des articles ici
public articles: ArticleRequest[] = [];

constructor(private articleService: ArticleService) {}

ngOnInit(): void {
  // Au chargement du composant, on récupère les articles
  this.articleService.getAllArticles().subscribe({
    next: (data) => {
      // data est un tableau d'articles
      this.articles = data;
    },
    error: (err) => {
      console.error('Erreur lors de la récupération des articles :', err);
    }
  });
}

}
