import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/interfaces/user.interfaces';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss']
})
export class MeComponent implements OnInit {
articleForm!: FormGroup;
userEmail: string = '';

  constructor(private fb: FormBuilder,private router: Router, private authService: AuthService) { }

  ngOnInit(): void {
     this.articleForm = this.fb.group({
      username: ['', Validators.required],
          email: ['', Validators.required]
        });
        this.authService.me().subscribe({
          next: (user: User) => {
            console.log(user);
            
            this.userEmail = user.email;
          },
          error: (err) => {
            console.error('Erreur lors du chargement des infos utilisateur :', err);
          }
        });
      
  }
  public onLogout(): void {
    // 1. Retirer le token du localStorage
    localStorage.removeItem('token');

    // 2. Rediriger l’utilisateur vers la page home (ou login, etc.)
    this.router.navigate(['/']);
  }
}
