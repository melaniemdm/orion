import { Component, Input, OnInit } from '@angular/core';
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
  constructor() { }

  ngOnInit(): void {
     
  }
  getUserName(): string {
   
    const user = this.users.find(u => u.id === this.article.auteur_id);
   
    return user ? user.user_name : 'Auteur inconnu';
  }
}
