import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { Theme } from 'src/app/interfaces/theme.interfaces';
import { User } from 'src/app/interfaces/user.interfaces';
import { AuthService } from 'src/app/services/auth.service';
import { SubscriptionService } from 'src/app/services/subscribe.service';
import { ThemeService } from 'src/app/services/theme.service';
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
   themes: Theme[] = []; 
  
  constructor(private fb: FormBuilder, private router: Router, private authService: AuthService, private userService: UserService, private themeService: ThemeService, private subscriptionService: SubscriptionService ) { }

  ngOnInit(): void {
    this.articleForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
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
        this.loadSubscribedThemes();
      },
      error: (err) => {
        console.error('Erreur lors du chargement des infos utilisateur :', err);
      }
    });
 
  }

  loadSubscribedThemes(): void {
    forkJoin({
      allThemes: this.themeService.getThemes(),
      userSubscriptions: this.subscriptionService.getUserSubscriptions()
    }).subscribe({
      next: ({ allThemes, userSubscriptions }) => {
      //  console.log('Structure de allThemes:', allThemes);
        console.log('allThemes est un tableau ?', Array.isArray(allThemes));
        console.log('allThemes reçu complet:', JSON.stringify(allThemes, null, 2));
        //console.log('Structure de userSubscriptions:', userSubscriptions);
        
        // Vérifier si les tableaux sont bien définis et non vides
        if (!Array.isArray(allThemes) || allThemes.length === 0) {
          console.warn('allThemes n\'est pas un tableau ou est vide');
        }
        
        if (!Array.isArray(userSubscriptions) || userSubscriptions.length === 0) {
          console.warn('userSubscriptions n\'est pas un tableau ou est vide');
          return;
        }
        
        // Vérifier les types des IDs
        console.log('Type de ID du premier thème:', typeof allThemes[0]?.id);
        console.log('Type de theme_id du premier abonnement:', typeof userSubscriptions[0]?.theme_id);
        
        const themesArray: Theme[] = Array.isArray(allThemes) ? allThemes : [];
        const subscribedThemeIds = userSubscriptions.map(sub => sub.theme_id);
        
        // Tester explicitement l'inclusion
        this.themes = themesArray.filter(theme => {
          const isIncluded = subscribedThemeIds.includes(theme.id);
          console.log(`Thème ID ${theme.id} (${theme.name_theme}) inclus dans ${JSON.stringify(subscribedThemeIds)}: ${isIncluded}`);
          return isIncluded;
        });
        
        console.log('Nombre de thèmes filtrés:', this.themes.length);
        console.log('Thèmes abonnés filtrés détail:', JSON.stringify(this.themes));
        
        // Vérification si Angular détecte le changement
        setTimeout(() => {
          console.log('Après détection des changements, taille de this.themes:', this.themes.length);
        }, 0);
      },
      error: (err) => {
        console.error('Erreur lors du chargement des thèmes abonnés :', err);
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
      const updatedData: Partial<User> = { user_name: formValues.username, email: formValues.email };
  
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
