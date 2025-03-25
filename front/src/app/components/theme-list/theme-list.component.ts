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
  selectedTheme = '';

  @Output() themeChange = new EventEmitter<string>();

  constructor(private themeService: ThemeService) {}

  ngOnInit(): void {
    this.themeService.getThemes().subscribe({
      next: (data: any) => {
        const rawThemes = Array.isArray(data) ? data : data?.subject ?? [];
        this.themes = rawThemes.map((t: any) => ({
          id: t.id,
          name_theme: t.name_theme,
          description: t.description
        }));
        this.selectedTheme = '';
      },
      error: err => console.error('Erreur lors du chargement des thèmes :', err)
    });
  }

  onThemeChange(themeId: string): void {
    this.themeChange.emit(themeId);
  }
}
