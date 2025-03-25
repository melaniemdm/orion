import { Component, EventEmitter, Input, OnInit, Output, SimpleChanges } from '@angular/core';
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
  @Input() showBackArrow: boolean = true;
  @Input() classInput: string = '';

  @Output() formSubmitted = new EventEmitter<FormGroup>();

  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
    this.subscriptionForm = this.fb.group({
      email: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });

    if (this.showUsername) {
      this.subscriptionForm.addControl(
        'username',
        this.fb.control('', Validators.required)
      );
    }
  }

  onSubmit(): void {
    if (this.subscriptionForm.invalid) {
      console.warn('Formulaire invalide :', this.subscriptionForm.errors);
      this.subscriptionForm.markAllAsTouched();
      return;
    }

    console.log('Formulaire soumis avec :', this.subscriptionForm.value);
    this.formSubmitted.emit(this.subscriptionForm);
  }

}
