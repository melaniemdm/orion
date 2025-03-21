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

  
  constructor(private subscriptionService: SubscriptionService, private authService: AuthService, private http: HttpClient) { }

  ngOnInit(): void {
  }
  toggleSubscription() {
  }
}
