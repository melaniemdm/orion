import { Component, EventEmitter, Input, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-formlogin',
  templateUrl: './formlogin.component.html',
  styleUrls: ['./formlogin.component.scss']
})
export class FormloginComponent implements OnInit {
  @Input() showBackArrow = true;
  @Input() titleForm = '';
  @Input() action = '';

  @Output() formSubmitted = new EventEmitter<FormGroup>();

  subscriptionForm!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.subscriptionForm = this.fb.group({
      email: ['', Validators.required], // peut contenir un email ou un username
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  onSubmit(): void {
    if (this.subscriptionForm.invalid) {
      console.warn('Formulaire invalide :', this.subscriptionForm.value);
      this.subscriptionForm.markAllAsTouched();
      return;
    }

    console.log('Données envoyées :', this.subscriptionForm.value);
    this.formSubmitted.emit(this.subscriptionForm);
  }
}
