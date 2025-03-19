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
  @Input() showUsername = true;
  @Input() action = '';

  @Output() formSubmitted = new EventEmitter<FormGroup>();
  subscriptionForm!: FormGroup;

  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
    this.subscriptionForm = this.fb.group({
      email: ['', [Validators.required]],  // <-- on peut renommer 'email' en 'identifier' pour accepter email ou username
      password: ['', [Validators.required, Validators.minLength(6)]],
    });

    
  }

  onSubmit(): void {
    console.log('Données envoyées :', this.subscriptionForm.value);

    if (this.subscriptionForm.invalid) {
      console.warn("Formulaire invalide :", this.subscriptionForm.errors); // Souvent null
      Object.keys(this.subscriptionForm.controls).forEach(key => {
        const control = this.subscriptionForm.get(key);
        if (control?.invalid) {
          console.warn(
            `- Le champ "${key}" est invalide :`,
            control.errors
          );
        }
      });
      return;
    }

    this.formSubmitted.emit(this.subscriptionForm);
  }
}
