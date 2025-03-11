import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss']
})
export class ButtonComponent implements OnInit {
  //Récupere la props
  @Input() action: string = '';
  @Input() routerLink?: string; 
  @Input() subscribed: boolean = false;
  
  @Output() clicked = new EventEmitter<void>();
  

  constructor() { }

  ngOnInit(): void {
  }
  onClick(): void {
    this.clicked.emit();
  }
}
