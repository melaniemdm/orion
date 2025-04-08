import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { forkJoin } from 'rxjs';
import { Theme } from 'src/app/interfaces/theme.interfaces';
import { User } from 'src/app/interfaces/user.interfaces';
import { AuthService } from 'src/app/services/auth.service';
import { SubscriptionService } from 'src/app/services/subscribe.service';
import { ThemeService } from 'src/app/services/theme.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss']
})
export class MeComponent implements OnInit {
  articleForm!: FormGroup;
  userId!: number;
  themes: Theme[] = [];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private userService: UserService,
    private themeService: ThemeService,
    private subscriptionService: SubscriptionService
  ) { }

  ngOnInit(): void {
    // Initialisation du formulaire
    this.articleForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, this.authService.passwordValidator]]
    });

    // Récuperation des infos de l'utilisateur connecté
    this.authService.me().subscribe({
      next: (user: User) => {
        this.userId = user.id;
        this.articleForm.patchValue({
          username: user.user_name,
          email: user.email
        });

        this.loadSubscribedThemes();
      },
      error: (err) => {
        console.error('Erreur lors du chargement des infos utilisateur :', err);
      }
    });
  }

  loadSubscribedThemes(): void {
    // Récupération parallèle de tous les thèmes et des abonnements de l’utilisateur
    forkJoin({
      allThemes: this.themeService.getThemes(),
      userSubscriptions: this.subscriptionService.getUserSubscriptions()
    }).subscribe({
      next: ({ allThemes, userSubscriptions }) => {
        const subscribedIds = userSubscriptions.map(s => s.theme_id);
        this.themes = allThemes.filter((theme: Theme) =>
          subscribedIds.includes(theme.id)
        );
      },
      error: (err) => {
        console.error('Erreur lors du chargement des thèmes abonnés :', err);
      }
    });
  }

  updateUser(): void {
    // Vérification que le formulaire est valide et que j'ai un userId
    if (this.articleForm.valid && this.userId) {
      const { username, email, password } = this.articleForm.value;
      // Préparation des données
      const updatedData: Partial<User> = {
        user_name: username,
        email
      };
      // Inclut le mot de passe que s’il est renseigné
      if (password && password.trim()) {
        updatedData.password = password;
      }
      // Appel de la méthode updateUser() du service
      this.userService.updateUser(this.userId, updatedData).subscribe({
        next: (updatedUser) => {

          // Réinitialiser le champ password
          this.articleForm.get('password')?.reset();
        },
        error: (err) => {
          console.error('Erreur lors de la mise à jour utilisateur :', err);
        }
      });
    } else {
      console.error('Formulaire invalide ou userId manquant.');
    }
  }
}
