import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss']
})
export class MeComponent implements OnInit {
articleForm!: FormGroup;
  constructor(private fb: FormBuilder,private router: Router) { }

  ngOnInit(): void {
     this.articleForm = this.fb.group({
      username: ['', Validators.required],
          email: ['', Validators.required]
        });
  }
  public onLogout(): void {
    // 1. Retirer le token du localStorage
    localStorage.removeItem('token');

    // 2. Rediriger l’utilisateur vers la page home (ou login, etc.)
    this.router.navigate(['/']);
  }
}
