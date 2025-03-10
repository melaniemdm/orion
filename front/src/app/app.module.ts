import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { HeaderComponent } from './components/header/header.component';
import { ButtonComponent } from './components/button/button.component';

import { SubscriptionComponent } from './pages/subscription/subscription.component';
import { LoginComponent } from './pages/login/login.component';
import { FormComponent } from './components/form/form.component';
import { InputFormComponent } from './components/input-form/input-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ArticleComponent } from './components/article/article.component';
import { ArticlesComponent } from './pages/articles/articles.component';
import { ThemesComponent } from './pages/themes/themes.component';
import { ThemeComponent } from './components/theme/theme.component';
import { NewArticlesComponent } from './pages/new-articles/new-articles.component';
import { MeComponent } from './pages/me/me.component';
import { CommentComponent } from './pages/comment/comment.component';
import { HttpClientModule } from '@angular/common/http';
import { ThemeListComponent } from './components/theme-list/theme-list.component';

@NgModule({
  declarations: [AppComponent, HomeComponent, HeaderComponent, ButtonComponent, SubscriptionComponent, LoginComponent, FormComponent, InputFormComponent, ArticleComponent, ArticlesComponent, ThemesComponent, ThemeComponent, NewArticlesComponent, MeComponent, CommentComponent, ThemeListComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
