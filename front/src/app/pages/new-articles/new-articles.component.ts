import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-new-articles',
  templateUrl: './new-articles.component.html',
  styleUrls: ['./new-articles.component.scss']
})
export class NewArticlesComponent implements OnInit {
  articleForm!: FormGroup;
  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
    this.articleForm = this.fb.group({
      theme: ['', Validators.required],
      title: ['', Validators.required],
      summary: ['', Validators.required]
    });

  }
  onSubmit(): void {
    if (this.articleForm.valid) {
      console.log('Article soumis', this.articleForm.value);
    }
  }
}
