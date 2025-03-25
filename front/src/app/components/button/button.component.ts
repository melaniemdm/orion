import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss']
})
export class ButtonComponent implements OnInit {
  @Input() action: string = '';
  @Input() routerLink?: string;
  @Input() subscribed: boolean = false;

  @Output() clicked = new EventEmitter<void>();
  ngOnInit(): void { }
  get buttonClass(): string {
    if (this.routerLink) return 'white';
    return this.subscribed ? 'gray' : 'purple';
  }

  handleClick(): void {
    if (!this.routerLink) {
      this.clicked.emit();
    }
  }
}
