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
  public newComment: string = '';  // Stocke le commentaire saisi
  public message: string = ''; // Message de succès ou d'erreur
  public isSuccess: boolean = false; // I
  public comments: any[] = [];// Stocke les commentaires récupérés

  constructor(private route: ActivatedRoute,
    private articleService: ArticleService) { }

  ngOnInit(): void {
    // 1) Récupére l'ID dans l'URL
    this.articleId = this.route.snapshot.params['id'];
    console.log("Article ID récupéré depuis l'URL :", this.articleId);
    if (!this.articleId) {
      console.error("Erreur : Aucun article ID trouvé dans l'URL !");
  }
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
    // Récupère les commentaires de l'article
    this.loadComments();
  }
   // Fonction pour charger les commentaires
   loadComments(): void {
    this.articleService.getComments(this.articleId).subscribe({
      next: (response: any) => {
        this.comments = response; // Stocke les commentaires récupérés
        console.log('Commentaires chargés :', this.comments);
      },
      error: (err) => {
        console.error("Erreur lors du chargement des commentaires :", err);
      }
    });
  }
// Envoie le commentaire au backend
  sendComment(): void {
    if (!this.newComment.trim()) {
      this.message = "Le commentaire ne peut pas être vide.";
      this.isSuccess = false;
      console.error("Erreur : Aucun texte dans le champ commentaire !");
      return;
    }

    this.articleService.postComment(this.articleId, this.newComment).subscribe({
      next: (response: any) => {
        //console.log('Commentaire envoyé avec succès :', response);
        this.message = "Commentaire envoyé avec succès !";
        this.isSuccess = true;
        this.newComment = '';  // Réinitialise le champ après l'envoi
        // Recharge les commentaires après l'envoi
        this.loadComments();
      },
      error: (err: any) => {
        console.error('Erreur lors de l\'envoi du commentaire :', err);
        this.message = "Erreur lors de l'envoi du commentaire.";
        this.isSuccess = false;
      }
    });
  }
}
