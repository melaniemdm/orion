import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { ArticleRequest } from 'src/app/interfaces/article.interfaces';
import { User } from 'src/app/interfaces/user.interfaces';
import { ArticleService } from 'src/app/services/article.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.scss']
})
export class ArticlesComponent implements OnInit {

// On stocke la liste des articles ici
public articles: ArticleRequest[] = [];
public users: User[] = [];

constructor(private articleService: ArticleService, private userService: UserService) {}

ngOnInit(): void {
  
  forkJoin([
    this.articleService.getAllArticles(),
    this.userService.getAllUsers()
  ]).subscribe({
    next: ([articles, users]) => {
      this.articles = articles;
      this.users = users;
      console.log('Liste des users reçus :', this.users);
      console.log('Liste des users reçus :', this.articles);
    },
    error: (err) => {
      console.error('Erreur lors de la récupération :', err);
    }
  });
}



}
