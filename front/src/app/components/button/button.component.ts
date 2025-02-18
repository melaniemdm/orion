import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss']
})
export class ButtonComponent implements OnInit {
  //Récupere la props
  @Input() action: string = '';
  @Input() routerLink?: string; 

  onClick(): void {
    console.log(`${this.action} button clicked`);
  }
 

  constructor() { }

  ngOnInit(): void {
  }

}
