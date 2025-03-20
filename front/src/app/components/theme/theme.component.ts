import { Component, Input, OnInit } from '@angular/core';
import { ArticleRequest } from 'src/app/interfaces/article.interfaces';

@Component({
  selector: 'app-theme',
  templateUrl: './theme.component.html',
  styleUrls: ['./theme.component.scss']
})
export class ThemeComponent implements OnInit {
  subscribed = false;
  @Input() article!: ArticleRequest;
  
  constructor() { }

  ngOnInit(): void {
  }
toggleSubscription() {
    this.subscribed = !this.subscribed;
  }
}
