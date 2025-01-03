import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {SessionService} from '../../../../services/session.service';
import {expect} from '@jest/globals';


describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceMock: any;
  let sessionServiceMock: any;
  let routerMock: any;

  beforeEach(() => {
    authServiceMock = {
      login: jest.fn()
    };

    sessionServiceMock = {
      logIn: jest.fn()
    };

    routerMock = {
      navigate: jest.fn()
    };

    TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        MatIconModule,
        BrowserAnimationsModule
      ],
      providers: [
        {provide: AuthService, useValue: authServiceMock},
        {provide: SessionService, useValue: sessionServiceMock},
        {provide: Router, useValue: routerMock}
      ]
    }).compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not display error message initially', () => {
    const errorElement = fixture.nativeElement.querySelector('.error');
    expect(errorElement).toBeNull();
  });

  it('should call AuthService.login and navigate on successful login', () => {
    const mockResponse = { token: 'test-token' };
    authServiceMock.login.mockReturnValue(of(mockResponse));
    component.form.setValue({
      email: 'mario@bros.com',
      password: 'password123'
    });
    component.submit();
    expect(authServiceMock.login).toHaveBeenCalledWith({
      email: 'mario@bros.com',
      password: 'password123'
    });
    expect(sessionServiceMock.logIn).toHaveBeenCalledWith(mockResponse);
    expect(routerMock.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should set onError to true when login fails', () => {
    authServiceMock.login.mockReturnValue(throwError(() => new Error('Login failed')));
    component.form.setValue({
      email: 'wrong@example.com',
      password: 'wrongpassword'
    });
    component.submit();
    expect(authServiceMock.login).toHaveBeenCalledWith({
      email: 'wrong@example.com',
      password: 'wrongpassword'
    });
    expect(component.onError).toBe(true);
    fixture.detectChanges();
    const errorElement = fixture.nativeElement.querySelector('.error');
    expect(errorElement).toBeTruthy();
    expect(errorElement.textContent).toContain('An error occurred');
  });

  it('should disable the submit button if the form is invalid', () => {
    component.form.setValue({
      email: 'non-email',
      password: ''
    });
    fixture.detectChanges();
    const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(submitButton.disabled).toBe(true);
  });

  it('should show validation error for required fields', () => {
    component.form.setValue({
      email: '',
      password: ''
    });
    component.form.markAllAsTouched();
    fixture.detectChanges();
    const emailInput = fixture.nativeElement.querySelector('input[formControlName="email"]');
    const passwordInput = fixture.nativeElement.querySelector('input[formControlName="password"]');
    expect(emailInput.classList).toContain('ng-invalid');
    expect(passwordInput.classList).toContain('ng-invalid');
  });

  it('should display error message when onError is true', () => {
    component.onError = true;
    fixture.detectChanges();
    const errorElement = fixture.nativeElement.querySelector('.error');
    expect(errorElement).toBeTruthy();
    expect(errorElement.textContent).toContain('An error occurred');
  });

});
