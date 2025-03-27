import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';
import { AuthSuccess } from 'src/app/interfaces/authAcces.interfaces';
import { RegisterRequest } from 'src/app/interfaces/registerRequest.interfaces';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-subscription',
  templateUrl: './subscription.component.html',
  styleUrls: ['./subscription.component.scss'], encapsulation: ViewEncapsulation.None
})
export class SubscriptionComponent implements OnInit {
  public onError = false;
  @Input('title-form') titleForm: string = '';
  public isLoggedIn: boolean = true;

  constructor(private authService: AuthService,
    private router: Router) { }

  ngOnInit(): void {

  }
  onFormSubmit(formValue: { user_name: string, email: string, password: string }): void {
    console.log("Formulaire reçu dans subscription.component.ts register:", formValue);

    const registerRequest: RegisterRequest = {
      user_name: formValue.user_name,
      email: formValue.email,
      password: formValue.password
    };

    this.authService.register(registerRequest).subscribe(
      (response: AuthSuccess) => {
        localStorage.setItem('token', response.token);
        setTimeout(() => { // petite temporisation pour être certain du stockage
          this.router.navigate(['/articles']);
        }, 100);
      },
      (error) => {
        console.error("Erreur inscription :", error);
        this.onError = true;
      }
    );
  }
}
