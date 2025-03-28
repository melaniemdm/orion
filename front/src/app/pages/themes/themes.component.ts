import { Component, OnInit } from '@angular/core';
import { Theme } from 'src/app/interfaces/theme.interfaces';
import { ThemeService } from 'src/app/services/theme.service';

@Component({
  selector: 'app-themes',
  templateUrl: './themes.component.html',
  styleUrls: ['./themes.component.scss']
})
export class ThemesComponent implements OnInit {
  themes: Theme[] = [];  // tableau pour stocker les thèmes


  constructor(private themeService: ThemeService) { }

  ngOnInit(): void {
    // Subscription à l'observable renvoyée par getThemes() pour récupérer la liste des thèmes
    this.themeService.getThemes().subscribe((themes: Theme[]) => {
      this.themes = themes; 
     
    });
  }


}

