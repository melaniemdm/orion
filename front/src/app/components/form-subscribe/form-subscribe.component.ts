import { Component, EventEmitter, Input, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-form-subscribe',
  templateUrl: './form-subscribe.component.html',
  styleUrls: ['./form-subscribe.component.scss']
})
export class FormSubscribeComponent implements OnInit {
  @Input() userForm!: FormGroup;
  @Input() formControlName!: string;
  @Input() required: boolean = false;
  @Input() titleForm: string = '';
  @Input() action: string = '';
  @Output() formSubmitted = new EventEmitter<{user_name: string, email: string, password: string}>();
  @Input() showBackArrow: boolean = true;


  constructor(private fb: FormBuilder) { }

  ngOnChanges(changes: SimpleChanges): void {
    this.ngOnInit();
  }
  ngOnInit(): void {
    this.userForm = this.fb.group({
      user_name: ['', Validators.required],
      email: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });

   
  }


  onSubmit(): void {
    if (this.userForm.invalid) {
      console.warn("Formulaire invalide :", this.userForm.errors);
      Object.keys(this.userForm.controls).forEach(key => {
        const controlErrors = this.userForm.get(key)?.errors;
        if (controlErrors) {
          console.warn(`Erreur sur ${key} :`, controlErrors);
        }
      });
      return;
    }
  
    const payload = {
      user_name: this.userForm.value.user_name, 
      email: this.userForm.value.email,
      password: this.userForm.value.password
    };
  
    console.log("Formulaire valide avec payload adapté envoyé vers API :", payload);
    this.formSubmitted.emit(payload);
  }

}
