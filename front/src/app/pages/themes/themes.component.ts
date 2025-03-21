import { Component, OnInit } from '@angular/core';
import { ArticleRequest } from 'src/app/interfaces/article.interfaces';
import { Theme } from 'src/app/interfaces/theme.interfaces';
import { ArticleService } from 'src/app/services/article.service';
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
    // Appel au service pour récupérer les thèmes
    this.themeService.getThemes().subscribe((response: any) => {
      // On suppose que `response.subject` est un tableau
      this.themes = response.subject;
      console.log('this.themes après mapping :', this.themes);
    });
    
    
  }
  }
 