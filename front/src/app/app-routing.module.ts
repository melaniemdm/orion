import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { SubscriptionComponent } from './pages/subscription/subscription.component';
import { LoginComponent } from './pages/login/login.component';
import { ArticlesComponent } from './pages/articles/articles.component';
import { ThemesComponent } from './pages/themes/themes.component';
import { NewArticlesComponent } from './pages/new-articles/new-articles.component';

// consider a guard combined with canLoad / canActivate route option
// to manage unauthenticated user to access private routes
const routes: Routes = [{ path: '', component: HomeComponent },
  {path: 'home', component: HomeComponent },
  { path: 'subscription', component: SubscriptionComponent },
  {path:'login', component: LoginComponent },
  {path:'articles', component: ArticlesComponent },
  {path:'themes', component: ThemesComponent },
  { path: 'newArticle', component: NewArticlesComponent }];
;

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
