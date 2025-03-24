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

// On stocke la liste des articles ici
public articles: ArticleRequest[] = [];
public users: User[] = [];
sortDescending: boolean = true;

constructor(private articleService: ArticleService, private userService: UserService, private router: Router) {}

ngOnInit(): void {
  const token = localStorage.getItem('token');

  if (!token) {
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
      this.sortArticles(); // Effectue le tri initial dès la récupération
    },
    error: (err) => {
      console.error('Erreur lors de la récupération :', err);
    }
  });
}


toggleSortOrder(): void {
  this.sortDescending = !this.sortDescending;
  this.sortArticles();
}
sortArticles(): void {
  this.articles.sort((a, b) => {
    const dateA = new Date(a.created_date).getTime();
    const dateB = new Date(b.created_date).getTime();

    return this.sortDescending ? dateB - dateA : dateA - dateB;
  });
}

}
