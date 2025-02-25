import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss']
})
export class MeComponent implements OnInit {
articleForm!: FormGroup;
  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
     this.articleForm = this.fb.group({
      username: ['', Validators.required],
          email: ['', Validators.required]
        });
  }

}
