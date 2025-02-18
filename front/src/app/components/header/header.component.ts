import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  
  // Contrôle l’affichage du menu en mobile
  public isMenuOpen: boolean = false;

  constructor() { }

  ngOnInit(): void { }

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

}
