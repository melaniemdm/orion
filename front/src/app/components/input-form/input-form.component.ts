import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-input-form',
  templateUrl: './input-form.component.html',
  styleUrls: ['./input-form.component.scss']
})
export class InputFormComponent implements OnInit {
  @Input() formGroup!: FormGroup;
  @Input() label = '';
  @Input() type = 'text';
  @Input() formControlName!: string;
  @Input() placeholder = '';
  @Input() required = false;
  @Input() classInput = '';
  ngOnInit(): void {
  }

}
