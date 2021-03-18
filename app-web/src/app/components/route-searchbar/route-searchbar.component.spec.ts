import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RouteSearchbarComponent } from './route-searchbar.component';

describe('RouteSearchbarComponent', () => {
  let component: RouteSearchbarComponent;
  let fixture: ComponentFixture<RouteSearchbarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RouteSearchbarComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RouteSearchbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
