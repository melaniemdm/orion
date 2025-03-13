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
  public users: any[] = [];
  public themes: any[] = [];


  constructor(private route: ActivatedRoute,
    private articleService: ArticleService) { }

  ngOnInit(): void {
    // 1) Récupére l'ID dans l'URL
    this.articleId = this.route.snapshot.params['id'];
    //console.log("Article ID récupéré depuis l'URL :", this.articleId);
    if (!this.articleId) {
     // console.error("Erreur : Aucun article ID trouvé dans l'URL !");
  }
    // 2) Appele articleService pour récupérer l'article correspondant
    this.articleService.getArticleById(this.articleId).subscribe({
      next: (response) => {
        
        this.articleSelected = response.post;
       // console.log('Article récupéré :', this.articleSelected);
       
      },
      error: (err) => {
        console.error('Erreur lors de la récupération de l\'article :', err);
      }
    });
    // Récupère les commentaires de l'article
    this.loadComments();
    this.loadUsers();
    this.loadThemes();
  }
// Charger la liste des utilisateurs
loadUsers(): void {
  this.articleService.getUsers().subscribe({
    next: (response: any) => { // <-- Ajoute ": any" pour éviter l'erreur de typage
      //console.log('Réponse utilisateurs avant stockage :', response);

      if (response && Array.isArray(response.user)) {
        this.users = response.user; // Extraire le tableau correct
      } else {
        console.error("Erreur : la réponse des utilisateurs n'est pas un tableau", response);
        this.users = [];
      }

      //console.log('Utilisateurs chargés :', this.users);
    },
    error: (err) => {
      console.error("Erreur lors du chargement des utilisateurs :", err);
    }
  });
}
  // Trouver le nom de l'utilisateur par son ID
  getUserName(auteurId: string): string {
    if (!this.users || this.users.length === 0) {
      return 'Utilisateur inconnu'; 
    }
  
    const user = this.users.find(u => u.id === Number(auteurId));
    //console.log("Utilisateur trouvé :", user);
  
    return user ? user.user_name : 'Utilisateur inconnu'; 
  }
   // Fonction pour charger les commentaires
   loadComments(): void {
    this.articleService.getComments(this.articleId).subscribe({
      next: (response: any) => {
        this.comments = response; // Stocke les commentaires récupérés
       //console.log('Commentaires chargés :', this.comments);
      },
      error: (err) => {
        //console.error("Erreur lors du chargement des commentaires :", err);
      }
    });
  }
// Envoie le commentaire au backend
  sendComment(): void {
    if (!this.newComment.trim()) {
      this.message = "Le commentaire ne peut pas être vide.";
      this.isSuccess = false;
     // console.error("Erreur : Aucun texte dans le champ commentaire !");
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
       // console.error('Erreur lors de l\'envoi du commentaire :', err);
        this.message = "Erreur lors de l'envoi du commentaire.";
        this.isSuccess = false;
      }
    });
  }

  getArticleAuthorName(): string {
    if (!this.articleSelected || !this.articleSelected.auteur_id || this.users.length === 0) {
      return 'Utilisateur inconnu';
    }
  
    const author = this.users.find(user => user.id === Number(this.articleSelected?.auteur_id));
    return author ? author.user_name : 'Utilisateur inconnu';
  }


  // Charger la liste des thèmes
  loadThemes(): void {
    this.articleService.getThemes().subscribe({
      next: (response) => {
       // console.log("Réponse brute des thèmes :", response);
        if (response && Array.isArray(response.subject)) {
          this.themes = response.subject; // Utiliser 'subject' au lieu de 'themes'
        } else {
          //console.error(" Erreur : la réponse des thèmes n'est pas un tableau", response);
          this.themes = [];
        }
  
        //console.log(" Thèmes chargés :", this.themes);
       
      },
      error: (err) => {
        console.error("Erreur lors du chargement des thèmes :", err);
      }
    });
  }
  
  

  getThemeName(themeId: number | undefined): string {
    if (!themeId) {
      return 'Thème inconnu';
    }
  
    //console.log(" Liste des thèmes :", this.themes);
    
    if (!Array.isArray(this.themes)) {
     // console.error(" Erreur : this.themes n'est pas un tableau", this.themes);
      return 'Thème inconnu';
    }
  
    const theme = this.themes.find(t => t.id === themeId);
    return theme ? theme.name_theme : 'Thème inconnu'; // Utiliser 'name_theme'
  }
  
  

}
