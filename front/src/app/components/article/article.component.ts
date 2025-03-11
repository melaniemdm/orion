import { Component, Input, OnInit } from '@angular/core';
import { ArticleRequest } from 'src/app/interfaces/article.interfaces';

@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.scss']
})
export class ArticleComponent implements OnInit {
  @Input() article!: ArticleRequest;
  constructor() { }

  ngOnInit(): void {
  }

}
