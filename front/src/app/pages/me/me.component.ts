import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/interfaces/user.interfaces';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss']
})
export class MeComponent implements OnInit {
  articleForm!: FormGroup;
  userEmail: string = '';
  userId!: number; // Stocke l'id du user courant
  updatedData:any;
  
  constructor(private fb: FormBuilder, private router: Router, private authService: AuthService, private userService: UserService) { }

  ngOnInit(): void {
    this.articleForm = this.fb.group({
      username: ['', Validators.required],
      email: [{ value: '', disabled: true }],
      password: [''],
    });
    this.authService.me().subscribe({
      next: (user: User) => {

        this.userId = user.id;
        this.userEmail = user.email;

        this.articleForm.patchValue({
          username: user.user_name,
          email: user.email
        });
      },
      error: (err) => {
        console.error('Erreur lors du chargement des infos utilisateur :', err);
      }
    });

  }
  public onLogout(): void {
    // 1. Retirer le token du localStorage
   // localStorage.removeItem('token');

    // 2. Rediriger l’utilisateur vers la page home (ou login, etc.)
    //this.router.navigate(['/']);
  }


  updateUser(): void {
    if (this.articleForm.valid && this.userId) {
  
      const formValues = this.articleForm.value;
  
      // Préparer les données à envoyer
      const updatedData: Partial<User> = { user_name: formValues.username };
  
      // N'inclure le mot de passe que s'il est renseigné (non vide)
      if (formValues.password && formValues.password.trim() !== '') {
        updatedData.password = formValues.password;
      }
  
      this.userService.updateUser(this.userId, updatedData).subscribe({
        next: (userUpdated) => {
          console.log('Utilisateur mis à jour avec succès:', userUpdated);
          this.articleForm.get('password')!.reset(); // réinitialiser le champ mot de passe après succès
        },
        error: (err) => {
          console.error('Erreur lors de la mise à jour utilisateur :', err);
        }
      });
  
    } else {
      console.error('Formulaire invalide ou ID utilisateur non défini.');
    }
  }
}
