import { Component, EventEmitter, Output } from '@angular/core';
import { Theme } from 'src/app/interfaces/theme.interfaces';
import {   ThemeService } from 'src/app/services/theme.service';

@Component({
  selector: 'app-theme-list',
  templateUrl: './theme-list.component.html',
  styleUrls: ['./theme-list.component.scss']
})
export class ThemeListComponent {
 
  themes: Theme[] = [];
  selectedTheme: string= "";

  @Output() themeChange = new EventEmitter<string>();

  constructor(private themeService: ThemeService) { }

  ngOnInit(): void {
    this.loadThemes();
  }

  loadThemes() {
    this.themeService.getThemes().subscribe({
      next: (response: any) => {
        if (Array.isArray(response)) {
          this.themes = response.map((t: any) => ({
            id: t.id,
            name_theme: t.name_theme,       // on reprend le même nom
            description: t.description
          })) as Theme[];
          
        } else if (response && response.subject) {
          this.themes = response.subject.map((theme: { id: any; name_theme: any; }) => ({
            id: theme.id,
            name_theme: theme.name_theme
          }));
        }
  
        console.log("Thèmes stockés après transformation :", this.themes);
  
       
        this.selectedTheme = ""; 
      },
      error: err => console.error("Erreur lors du chargement des thèmes", err)
    });
  }

  onThemeChange(value: string) {
    console.log("ngOnInit() exécuté !");
    this.themeChange.emit(value);
  }

}
