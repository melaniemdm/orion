import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  
  // Contrôle l’affichage du menu en mobile
  public isMenuOpen: boolean = false;
  public isLoggedIn: boolean = false;

  constructor(private router: Router) { }

  ngOnInit(): void { 
    this.isLoggedIn = !!localStorage.getItem('token');
  }

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }
  public onLogout(): void {
    // 1. Retirer le token du localStorage
    localStorage.removeItem('token');

    // 2. Rediriger l’utilisateur vers la page home (ou login, etc.)
    this.router.navigate(['/']);
  }
}
