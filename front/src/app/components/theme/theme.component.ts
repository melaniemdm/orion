import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { ArticleRequest } from 'src/app/interfaces/article.interfaces';
import { Theme } from 'src/app/interfaces/theme.interfaces';
import { AuthService } from 'src/app/services/auth.service';
import { SubscriptionService } from 'src/app/services/subscribe.service';

@Component({
  selector: 'app-theme',
  templateUrl: './theme.component.html',
  styleUrls: ['./theme.component.scss']
})
export class ThemeComponent implements OnInit {
  subscribed = false;
  @Input() theme!: Theme;
  @Input() context: 'default' | 'me' = 'default';
  
  constructor(private subscriptionService: SubscriptionService, private authService: AuthService, private http: HttpClient) { }

  ngOnInit(): void {
    this.subscriptionService.getUserSubscriptions().subscribe(subscriptions => {
      this.subscribed = subscriptions.some(sub => sub.theme_id === this.theme.id);
      console.log('État initial abonnement :', this.subscribed);
    });
  }
  
 

  // Vérifie si l’utilisateur est déjà abonné à ce thème
  toggleSubscription(): void {
    console.log('État abonnement avant action :', this.subscribed);

    if (!this.subscribed) {
      console.log('Tentative abonnement au thème ID :', this.theme.id);
      this.subscriptionService.subscribeToTheme(this.theme.id)
        .subscribe(() => {
          this.subscribed = true;
          console.log('Abonnement réussi au thème ID :', this.theme.id);
        }, err => {
          console.error('Erreur abonnement (subscribeToTheme) :', err);
        });
    } else {
      console.log('Tentative désabonnement du thème ID :', this.theme.id);
      this.subscriptionService.unsubscribeFromTheme(this.theme.id)
        .subscribe(() => {
          this.subscribed = false;
          console.log('Désabonnement réussi du thème ID :', this.theme.id);
        }, err => {
          console.error('Erreur désabonnement (unsubscribeFromTheme) :', err);
        });
    }
  }

  getButtonLabel(): string {
    if (this.context === 'me') {
      return 'Se désabonner';
    }
    return this.subscribed ? 'Déjà abonné' : 'S\'abonner';
  }
}

