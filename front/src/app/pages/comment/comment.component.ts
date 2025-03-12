import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ArticleRequest } from 'src/app/interfaces/article.interfaces';
import { ArticleService } from 'src/app/services/article.service';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.scss']
})
export class CommentComponent implements OnInit {
  public articleId!: string;
  public articleSelected?: ArticleRequest;

  constructor(private route: ActivatedRoute,
    private articleService: ArticleService) { }

  ngOnInit(): void {
    // 1) Récupére l'ID dans l'URL
    this.articleId = this.route.snapshot.params['id'];

    // 2) Appele articleService pour récupérer l'article correspondant
    this.articleService.getArticleById(this.articleId).subscribe({
      next: (response) => {
        
        this.articleSelected = response.post;
        console.log('Article récupéré :', this.articleSelected);
       
      },
      error: (err) => {
        console.error('Erreur lors de la récupération de l\'article :', err);
      }
    });
  }

}
