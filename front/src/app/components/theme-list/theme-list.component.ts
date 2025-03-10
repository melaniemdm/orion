import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-theme-list',
  templateUrl: './theme-list.component.html',
  styleUrls: ['./theme-list.component.scss']
})
export class ThemeListComponent {
 

// Après
public subjectData = {
  subject: [
    { id: 1, name_theme: 'informatique' },
    { id: 2, name_theme: 'java' },
    { id: 3, name_theme: 'javascript' },
    { id: 4, name_theme: 'spring' },
    { id: 5, name_theme: 'springboot' },
    { id: 6, name_theme: 'go' },
    { id: 7, name_theme: 'rust' }
  ]
};

//utliser le back end pour recuperer la liste des theme
//dans la liste deroulante je pourrais recuperer l'id
// html a modifier

  public selectedTheme: string = ''; // ou null, mais '' est plus simple

  @Output() themeChange = new EventEmitter<string>();
  onThemeChange(value: string) {
    this.themeChange.emit(value);
  }

}
