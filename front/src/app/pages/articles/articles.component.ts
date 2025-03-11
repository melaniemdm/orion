import { Component, OnInit } from '@angular/core';
import { Article, ArticleService } from 'src/app/services/article.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.scss']
})
export class ArticlesComponent implements OnInit {

  articles: Article[] = [];

  constructor(private articleService: ArticleService, private userService: UserService) {}

  ngOnInit(): void {
    this.loadArticles();
  }

  loadArticles() {
    this.articleService.getArticles().subscribe({
      next: (data) => {
        if (Array.isArray(data)) {
          this.articles = data;
        } else {
          console.error("❌ La réponse du backend n'est pas un tableau :", data);
        }
        console.log("🎯 Articles stockés :", this.articles);
      },
      error: (err) => console.error("❌ Erreur lors du chargement des articles", err)
    });
  }
}
