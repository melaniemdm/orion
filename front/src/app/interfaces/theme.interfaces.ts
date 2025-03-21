export interface Theme {
    id: number;
    name_theme: string;
    description?: string;
  }
  export interface ThemeResponse {
    subject?: Theme[]; 
  }