import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.scss']
})
export class FormComponent implements OnInit {


subscriptionForm!: FormGroup;
@Input() titleForm: string = '';
@Input() action: string = '';
@Input() showUsername: boolean = true; 


  constructor(private fb: FormBuilder) { }
  ngOnChanges(changes: SimpleChanges): void {
    this.ngOnInit();
  }
  ngOnInit(): void {
    this.subscriptionForm = this.fb.group({
      username: this.showUsername ? ['', Validators.required] : null, 
      email: ['', [Validators.required, Validators.email]], 
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  onSubmit(): void {
    if (this.subscriptionForm.valid) {
      console.log('Formulaire soumis', this.subscriptionForm.value);
    }
  }

}
