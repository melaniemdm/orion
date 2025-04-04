import { Component, EventEmitter, Input, OnInit, Output, SimpleChanges } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';

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

  constructor(private fb: FormBuilder, 
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.userForm = this.fb.group({
      user_name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, this.authService.passwordValidator]]
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

