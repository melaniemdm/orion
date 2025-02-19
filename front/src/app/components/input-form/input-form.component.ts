import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-input-form',
  templateUrl: './input-form.component.html',
  styleUrls: ['./input-form.component.scss']
})
export class InputFormComponent implements OnInit {
  @Input() formGroup!: FormGroup;
  @Input() for: string = '';
  @Input() label: string = '';
  @Input() type: string = '';
  @Input() id: string = '';
  @Input() formControlName!: string;
  constructor() { }

  ngOnInit(): void {
  }

}
