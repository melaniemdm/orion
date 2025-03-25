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
  @Input() theme!: Theme;
  @Input() context: 'default' | 'me' = 'default';

  subscribed = false;
  subscriptions: { id: number; theme_id: number; user_id: number }[] = [];

  get buttonLabel(): string {
    if (this.context === 'me') return 'Se désabonner';
    return this.subscribed ? 'Déjà abonné' : 'S’abonner';
  }

  constructor(private subscriptionService: SubscriptionService) {}

  ngOnInit(): void {
    this.loadSubscriptions();
  }

  toggleSubscription(): void {
    const themeId = this.theme.id;

    if (!this.subscribed) {
      this.subscriptionService.subscribeToTheme(themeId).subscribe({
        next: () => {
          this.subscribed = true;
          this.loadSubscriptions();
        },
        error: err => console.error('Erreur abonnement :', err)
      });
    } else {
      const subscription = this.subscriptions.find(sub => sub.theme_id === themeId);
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

  private loadSubscriptions(): void {
    this.subscriptionService.getUserSubscriptions().subscribe(subs => {
      this.subscriptions = subs;
      this.subscribed = subs.some(sub => sub.theme_id === this.theme.id);
    });
  }
}

