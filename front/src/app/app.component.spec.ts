import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {MatToolbarModule} from '@angular/material/toolbar';
import {RouterTestingModule} from '@angular/router/testing';
import {expect} from '@jest/globals';
import {AppComponent} from './app.component';
import {AuthService} from './features/auth/services/auth.service';
import {Router} from '@angular/router';
import {SessionService} from './services/session.service';
import {of} from 'rxjs';

let mockAuthService = {isAuthenticated: jest.fn()};
let mockRouter = {navigate: jest.fn()};
let mockSessionService = {
  $isLogged: jest.fn(),
  logOut: jest.fn(),
};

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ], providers: [
        {provide: AuthService, useValue: mockAuthService},
        {provide: Router, useValue: mockRouter},
        {provide: SessionService, useValue: mockSessionService},
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should call $isLogged from sessionService', () => {
    const isLoggedSpy = jest.spyOn(mockSessionService, '$isLogged').mockReturnValue(of(true));
    component.$isLogged().subscribe((result) => {
      expect(result).toBe(true);
    });
    expect(isLoggedSpy).toHaveBeenCalled();
  });

  it('should call logOut and navigate to the root on logout', () => {
    const logOutSpy = jest.spyOn(mockSessionService, 'logOut');
    const navigateSpy = jest.spyOn(mockRouter, 'navigate');
    component.logout();
    expect(logOutSpy).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['']);
  });
});
