import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
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
  // Variable pour la gestion d'état utilisateur
  public isLoggedIn: boolean = true;

  // Input pour le titre du formulaire
  @Input('title-form') titleForm: string = '';

  public form: FormGroup = this.fb.group({
    user_name: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
  });

  constructor(private authService: AuthService,
    private router: Router, private fb: FormBuilder) { }

  ngOnInit(): void { }

  // Méthode pour gérer le submit du formulaire
  onFormSubmit(form: FormGroup): void {
    if (form.valid) {
      const registerRequest: RegisterRequest = form.value;

      // Ici, on utilise bien 'register' pour l'inscription
      this.authService.register(registerRequest).subscribe(
        (response: AuthSuccess) => {
          localStorage.setItem('token', response.token);
          console.log('token', response.token || "pas de token");
          setTimeout(() => {
            this.router.navigate(['/articles']);
          }, 100);

        },
        error => {
          console.error("Erreur lors de l'inscription :", error);
          this.onError = true;
        }
      );
    } else {
      console.warn("Formulaire invalide :", form.errors);
      form.markAllAsTouched();
    }
  }
}
