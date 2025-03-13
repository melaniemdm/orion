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
@Output() formSubmitted = new EventEmitter<FormGroup>();
@Input() showBackArrow: boolean = true;
@Input() showTitleForm : boolean=true;


  constructor(private fb: FormBuilder) { }
  ngOnChanges(changes: SimpleChanges): void {
    this.ngOnInit();
  }
  ngOnInit(): void {
    this.subscriptionForm = this.fb.group({
      email: ['', [Validators.required]], 
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  
    // Ajoute `username` uniquement si `showUsername` est activé 
    if (this.showUsername) {
      this.subscriptionForm.addControl('username', this.fb.control('', Validators.required));
    }
  }
  

  onSubmit(): void {
    console.log("Formulaire soumis !");
  console.log("Données envoyées :", this.subscriptionForm.value);
  
  if (this.subscriptionForm.invalid) {
    console.warn(" Formulaire invalide :", this.subscriptionForm.errors);
    
    Object.keys(this.subscriptionForm.controls).forEach(key => {
      const controlErrors = this.subscriptionForm.get(key)?.errors;
      if (controlErrors) {
        console.warn(`Erreur sur ${key} :`, controlErrors);
      }
    });
    
    return;
  }

  this.formSubmitted.emit(this.subscriptionForm);}

}
