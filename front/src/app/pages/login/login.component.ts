import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthSuccess } from 'src/app/interfaces/authAcces.interfaces';
import { LoginRequest } from 'src/app/interfaces/login.interfaces';
import { AuthService } from 'src/app/services/auth.service';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  @Input() loginForm!: FormGroup;
  public isLoggedIn: boolean = true;
 
  public hide = true;
  public onError = false;
 
  public form: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(3)]]
  });
 
  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router,
    private sessionService: SessionService) { }

  ngOnInit(): void {

  }

  onFormSubmit(form: FormGroup): void {
   
    if (form.valid) {
      const loginRequest = form.value as LoginRequest;

      this.authService.login(form.value).subscribe(
        (response: AuthSuccess) => {
        
          localStorage.setItem('token', response.token);
         

          this.authService.me().subscribe((user) => {
            this.sessionService.logIn(user);
            
            this.router.navigate(['/articles']);
          });
        },
        error => {
          console.error("Erreur de connexion :", error);
          this.onError = true;
        }
      );
    } else {
      console.warn("Formulaire invalide :", form.errors);
    }
  }

}
