import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewArticlesComponent } from './new-articles.component';

describe('NewArticlesComponent', () => {
  let component: NewArticlesComponent;
  let fixture: ComponentFixture<NewArticlesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewArticlesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewArticlesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
