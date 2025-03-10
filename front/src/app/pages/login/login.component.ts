import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthSuccess } from 'src/app/interfaces/authAcces.interfaces';
import { LoginRequest } from 'src/app/interfaces/login.interfaces';
import { User } from 'src/app/interfaces/user.interfaces';
import { AuthService } from 'src/app/services/auth.service';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  /**
   * Indique si le mot de passe est masqué ou non dans l'UI.
   */
  public hide = true;
  /**
  * Indicateur d'erreur pour l'affichage conditionnel d'un message d'erreur dans l'UI.
  */
  public onError = false;
  /**
     * FormGroup réactif pour gérer la saisie de l'email et du mot de passe.
     */
  public form: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(3)]]
  });
  /**
   * Constructeur : injection des dépendances.
   */
  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router,
    private sessionService: SessionService) { }

  ngOnInit(): void {
    
  }

  onFormSubmit(form: FormGroup): void {
    console.log("📩 Formulaire reçu dans login.component.ts login:", form.value);

    if (form.valid) {
      const loginRequest = form.value as LoginRequest;

      this.authService.login(loginRequest).subscribe(
        (response: AuthSuccess) => {
          console.log("✅ Connexion réussie :", response);
          localStorage.setItem('token', response.token);
          console.log("📢 Token stocké :", localStorage.getItem('token')); // ✅ Vérifier si le token stocké est correct

          this.authService.me().subscribe((user) => {
            this.sessionService.logIn(user);
            console.log("🔀 Redirection vers /articles...");
            this.router.navigate(['/articles']);
          });
        },
        error => {
          console.error("❌ Erreur de connexion :", error);
          this.onError = true;
        }
      );
    } else {
      console.warn("⚠️ Formulaire invalide :", form.errors);
    }
  }

}
