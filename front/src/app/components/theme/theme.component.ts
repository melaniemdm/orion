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
  subscriptions: {id: number, theme_id: number, user_id: number}[] = [];
  
  constructor(private subscriptionService: SubscriptionService, private authService: AuthService, private http: HttpClient) { }

  ngOnInit(): void {
    this.subscriptionService.getUserSubscriptions().subscribe(subscriptions => {
      this.subscriptions = subscriptions;
      this.subscribed = subscriptions.some(sub => sub.theme_id === this.theme.id);
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
          
          // Recharge les abonnements après abonnement
          this.subscriptionService.getUserSubscriptions().subscribe(subscriptions => {
            this.subscriptions = subscriptions;
          });
  
        }, err => {
          console.error('Erreur abonnement (subscribeToTheme) :', err);
        });
  
    } else {
      console.log('Tentative désabonnement du thème ID :', this.theme.id);
  
      // Important : récupérer subscriptionId au lieu de themeId pour supprimer
      const subscription = this.subscriptions.find(sub => sub.theme_id === this.theme.id);
      if (subscription) {
        this.subscriptionService.unsubscribeFromTheme(subscription.id)
          .subscribe(() => {
            this.subscribed = false;
            console.log('Désabonnement réussi, subscriptionId :', subscription.id);
  
            // Recharge les abonnements après désabonnement
            this.subscriptionService.getUserSubscriptions().subscribe(subscriptions => {
              this.subscriptions = subscriptions;
            });
  
          }, err => {
            console.error('Erreur désabonnement (unsubscribeFromTheme) :', err);
          });
      } else {
        console.error('Abonnement introuvable pour ce thème. Vérifie tes données.');
      }
    }
  }
  

  getButtonLabel(): string {
    if (this.context === 'me') {
      return 'Se désabonner';
    }
    return this.subscribed ? 'Déjà abonné' : 'S\'abonner';
  }
}

