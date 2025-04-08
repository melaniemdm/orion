import { Component, Input, OnInit } from '@angular/core';
import { Theme } from 'src/app/interfaces/theme.interfaces';
import { SubscriptionService } from 'src/app/services/subscribe.service';

@Component({
  selector: 'app-theme',
  templateUrl: './theme.component.html',
  styleUrls: ['./theme.component.scss']
})
export class ThemeComponent implements OnInit {
  @Input() theme!: Theme;
  @Input() context: 'default' | 'me' = 'default';
  @Input() allowUnsubscribe: boolean = true;

  subscribed = false;
  subscriptions: { id: number; theme_id: number; user_id: number }[] = [];

  get buttonLabel(): string {
    if (this.context === 'me') return 'Se désabonner';
    return this.subscribed ? 'Déjà abonné' : 'S’abonner';
  }

  constructor(private subscriptionService: SubscriptionService) { }

  ngOnInit(): void {
    this.loadSubscriptions();
  }

  toggleSubscription(): void {
    // Récupère l'id du thème
    const themeId = this.theme.id;
    // Si l'user n'est pas encore abonné au thème
    if (!this.subscribed) {
      this.subscriptionService.subscribeToTheme(themeId).subscribe({
        next: () => {
          this.subscribed = true;
          // recharge la liste des abonnements
          this.loadSubscriptions();
        },
        error: err => console.error('Erreur abonnement :', err)
      });
    } else {
      // Si l'utilisateur est déjà abonné et que le désabonnement est autorisé
      if (this.allowUnsubscribe) {
        // Cherche l'abonnement correspondant au thème
        const subscription = this.subscriptions.find(sub => sub.theme_id === themeId);
        //Si aucun abonnement trouvé on ne fait rien
        if (!subscription) return;

        this.subscriptionService.unsubscribeFromTheme(subscription.id).subscribe({
          next: () => {
            this.subscribed = false;
            this.loadSubscriptions();
            window.location.reload();
          },
          error: err => console.error('Erreur désabonnement :', err)
        });
      }

    }
  }

  private loadSubscriptions(): void {
    this.subscriptionService.getUserSubscriptions().subscribe(subs => {
      this.subscriptions = subs;
      this.subscribed = subs.some(sub => sub.theme_id === this.theme.id);
    });
  }
}

