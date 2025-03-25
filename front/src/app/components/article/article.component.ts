import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ArticleRequest } from 'src/app/interfaces/article.interfaces';
import { User } from 'src/app/interfaces/user.interfaces';

@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.scss']
})
export class ArticleComponent implements OnInit {
  @Input() article!: ArticleRequest;
  @Input() users: User[] = [];

  constructor(private router: Router) { }
  ngOnInit(): void { }
  get userName(): string {
    return this.users.find(user => user.id === this.article.auteur_id)?.user_name ?? 'Auteur inconnu';
  }

  goToComments(): void {
    this.router.navigate(['/comment', this.article.id]);
  }
}

