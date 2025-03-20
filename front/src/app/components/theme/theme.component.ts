import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { ArticleRequest } from 'src/app/interfaces/article.interfaces';
import { AuthService } from 'src/app/services/auth.service';
import { SubscriptionService } from 'src/app/services/subscribe.service';

@Component({
  selector: 'app-theme',
  templateUrl: './theme.component.html',
  styleUrls: ['./theme.component.scss']
})
export class ThemeComponent implements OnInit {
  subscribed = false;
  @Input() article!: ArticleRequest;

  
  constructor(private subscriptionService: SubscriptionService, private authService: AuthService, private http: HttpClient) { }

  ngOnInit(): void {
  }
  toggleSubscription() {
    console.log('toggleSubscription() appelé');
    console.log('Article actuel:', this.article);
    console.log('article.theme_id =', this.article.theme_id);

    // Bascule l'état local
    this.subscribed = !this.subscribed;
    console.log('Nouvelle valeur de "subscribed":', this.subscribed);

    // Récupère le token depuis AuthService
    const token = this.authService.getToken();
    console.log('Token récupéré depuis AuthService:', token);

    // Crée l'entête Authorization
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    console.log('Headers utilisés:', headers);

    if (this.subscribed) {
      // Abonnement
      const body = { theme_id: this.article.theme_id };
      console.log('Envoi POST vers /api/subscription avec le body:', body);

      this.http.post('/api/subscription', body, { headers })
        .subscribe({
          next: (res) => {
            console.log('Abonnement réussi:', res);
          },
          error: (err) => {
            console.error('Erreur lors de l’abonnement:', err);
            this.subscribed = false; // revert en cas d'erreur
          }
        });
    } 
  }
}
