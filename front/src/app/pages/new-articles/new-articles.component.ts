import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ArticleRequest } from 'src/app/interfaces/article.interfaces';
import { AuthSuccess } from 'src/app/interfaces/authAcces.interfaces';
import { ArticleService } from 'src/app/services/article.service';
import { AuthService } from 'src/app/services/auth.service'; // <-- importer l'AuthService
import { User } from 'src/app/interfaces/user.interfaces';

@Component({
  selector: 'app-new-articles',
  templateUrl: './new-articles.component.html',
  styleUrls: ['./new-articles.component.scss']
})
export class NewArticlesComponent implements OnInit {
  public articleForm!: FormGroup;
  public onError = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private articleService: ArticleService,
    private authService: AuthService // <-- injection de AuthService
  ) { }

  ngOnInit(): void {
    this.articleForm = this.fb.group({
      theme_id: ['', Validators.required],
      title: ['', Validators.required],
      description: ['', Validators.required]
    });
  }

  public onSubmit(): void {
    this.onFormSubmit(this.articleForm);
  }

  public onFormSubmit(form: FormGroup): void {


    if (form.valid) {
      const registerRequest = form.value as ArticleRequest;


      // Récupération des infos du user via authService.me()
      this.authService.me().subscribe({
        next: (user: User) => {

          registerRequest.auteur_id = user.id;


          // Appel au service ArticleService pour créer l'article
          this.articleService.registerArticle(registerRequest).subscribe({
            next: (response: AuthSuccess) => {

              if (response.token) {
                localStorage.setItem('token', response.token);
              } else {
                console.warn("Aucun token renvoyé lors de la création d'article, on ne modifie pas le localStorage.");
              }

              
              this.router.navigate(['/articles']).then(success => {
                if (success) {
                  console.log("Redirection réussie !");
                } else {
                  console.error("Problème de redirection !");
                }
              });
            },
            error: (err) => {
              console.error("Erreur lors de la création de l'article :", err);
              this.onError = true;
            }
          });
        },
        error: (err) => {
          console.error("Erreur lors de la récupération du user :", err);
          this.onError = true;
        }
      });

    } else {
      console.warn("Formulaire invalide :", form.errors);
    }
  }

  // Mise à jour de 'theme_id' avec la valeur du thème sélectionné
  public onThemeSelected(themeValue: string): void {
    this.articleForm.patchValue({ theme_id: themeValue });
  }
}
