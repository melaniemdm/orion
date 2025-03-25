import { Component, EventEmitter, Input, OnInit, Output, SimpleChanges } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';

@Component({
  selector: 'app-form-subscribe',
  templateUrl: './form-subscribe.component.html',
  styleUrls: ['./form-subscribe.component.scss']
})
export class FormSubscribeComponent implements OnInit {
  @Input() titleForm: string = '';
  @Input() action: string = '';
  @Input() showBackArrow: boolean = true;

  @Output() formSubmitted = new EventEmitter<{
    user_name: string;
    email: string;
    password: string;
  }>();

  public userForm!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.userForm = this.fb.group({
      user_name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, passwordValidator]]
    });
  }

  onSubmit(): void {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched(); // Active les erreurs visuelles dans le template
      console.warn('Formulaire invalide :', this.userForm.errors);
      return;
    }

    const payload = this.userForm.value;
    console.log('Formulaire valide, payload envoyé :', payload);
    this.formSubmitted.emit(payload);
  }
}

// Personnalisation du mot de passe
export function passwordValidator(control: AbstractControl): ValidationErrors | null {
  const value = control.value || '';

  const hasUpperCase = /[A-Z]/.test(value);
  const hasLowerCase = /[a-z]/.test(value);
  const hasNumber = /\d/.test(value);
  const hasSpecialChar = /[^A-Za-z0-9]/.test(value);
  const hasMinLength = value.length >= 8;

  const isValid = hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar && hasMinLength;

  return isValid
    ? null
    : {
        passwordInvalid: true
      };

}