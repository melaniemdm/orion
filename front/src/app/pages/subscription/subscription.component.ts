import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthSuccess } from 'src/app/interfaces/authAcces.interfaces';
import { RegisterRequest } from 'src/app/interfaces/registerRequest.interfaces';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-subscription',
  templateUrl: './subscription.component.html',
  styleUrls: ['./subscription.component.scss'],encapsulation: ViewEncapsulation.None
})
export class SubscriptionComponent implements OnInit {
  public onError = false;

  constructor(private authService: AuthService,
    private router: Router) { }

  ngOnInit(): void {
    
  }
  onFormSubmit(form: FormGroup): void {
    console.log("Formulaire reçu dans subscription.component.ts register:", form.value);

    if (form.valid) {
      const registerRequest = form.value as RegisterRequest;

      console.log("Tentative d'inscription :", registerRequest);

      this.authService.register(registerRequest).subscribe(
        (response: AuthSuccess) => {
          console.log("Inscription réussie :", response);
          localStorage.setItem('token', response.token);
          console.log("Redirection vers /articles...");
          this.router.navigate(['/articles']).then(success => {
            if (success) {
              console.log("Redirection réussie !");
            } else {
              console.error("Problème de redirection !");
            }
          });
        },
        error => {
          console.error("Erreur d'inscription :", error);
          this.onError = true;
        }
      );
    } else {
      console.warn("⚠️ Formulaire invalide :", form.errors);
    }
  }
}
