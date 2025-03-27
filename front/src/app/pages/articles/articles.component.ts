import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
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
  articles: ArticleRequest[] = [];
  users: User[] = [];
  sortDescending = true;

  constructor(
    private articleService: ArticleService,
    private userService: UserService,
    private router: Router
  ) { }

  ngOnInit(): void {
    if (!localStorage.getItem('token')) {
      this.router.navigate(['/home']);
      return;
    }

    forkJoin([
      this.articleService.getAllArticles(),
      this.userService.getAllUsers()
    ]).subscribe({
      next: ([articles, users]) => {
        this.articles = articles;
        this.users = users;
        this.sortArticles();
      },
      error: (err) => console.error('Erreur chargement articles/utilisateurs :', err)
    });
  }

  //Inverse la valeur de tri
  toggleSortOrder(): void {
    this.sortDescending = !this.sortDescending;
    this.sortArticles();
  }

  // Tri les articles selon leur date de création
  private sortArticles(): void {
    this.articles.sort((a, b) =>
      this.sortDescending
        ? new Date(b.created_date).getTime() - new Date(a.created_date).getTime()
        : new Date(a.created_date).getTime() - new Date(b.created_date).getTime()
    );
  }

}
