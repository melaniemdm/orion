import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.scss']
})
export class FormComponent implements OnInit {


subscriptionForm!: FormGroup;
@Input() titleForm: string = '';
  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
    this.subscriptionForm = this.fb.group({
      username: ['', Validators.required], 
      email: ['', [Validators.required, Validators.email]], 
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.subscriptionForm.valid) {
      console.log('Formulaire soumis', this.subscriptionForm.value);
    }
  }

}
