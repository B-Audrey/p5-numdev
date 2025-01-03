import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {expect} from '@jest/globals';

import {RegisterComponent} from './register.component';
import {Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {MatButtonModule} from '@angular/material/button';
import {of, throwError} from 'rxjs';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authServiceMock: any;
  let routerMock: any;

  beforeEach(() => {
    authServiceMock = {
      register: jest.fn()
    };

    routerMock = {
      navigate: jest.fn()
    };

    TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        BrowserAnimationsModule
      ],
      providers: [
        {provide: AuthService, useValue: authServiceMock},
        {provide: Router, useValue: routerMock}
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not display error message when form has not been touched', () => {
    const errorElement = fixture.nativeElement.querySelector('.error');
    expect(errorElement).toBeNull();
  });

  it('should call AuthService.register and navigate on successful registration', () => {
    authServiceMock.register.mockReturnValue(of({}));

    component.form.setValue({
      firstName: 'Mario',
      lastName: 'Bros',
      email: 'mario@bros.com',
      password: 'password123'
    });

    component.submit();

    expect(authServiceMock.register).toHaveBeenCalledWith({
      firstName: 'Mario',
      lastName: 'Bros',
      email: 'mario@bros.com',
      password: 'password123'
    });
    expect(routerMock.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should set onError to true when registration fails', () => {
    authServiceMock.register.mockReturnValue(throwError(() => new Error('Registration failed')));
    component.form.setValue({
      firstName: 'Mario',
      lastName: 'Bros',
      email: 'mario@bros.com',
      password: 'password123'
    });
    component.submit();
    expect(authServiceMock.register).toHaveBeenCalledWith({
      firstName: 'Mario',
      lastName: 'Bros',
      email: 'mario@bros.com',
      password: 'password123'
    });
    expect(component.onError).toBe(true);
    fixture.detectChanges();
    const errorElement = fixture.nativeElement.querySelector('.error');
    expect(errorElement).toBeTruthy();
    expect(errorElement.textContent).toContain('An error occurred');
  });

  it('should disable the submit button if the form is invalid', () => {
    component.form.setValue({
      firstName: 'a',
      lastName: 'b',
      email: 'invalid-email',
      password: 'c'
    });
    fixture.detectChanges();
    const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(submitButton.disabled).toBe(true);
  });

  it('should show errors for empty required fields', () => {
    component.form.setValue({
      firstName: '',
      lastName: '',
      email: '',
      password: ''
    });
    component.form.markAllAsTouched();
    fixture.detectChanges();
    const firstNameInput = fixture.nativeElement.querySelector('input[formControlName="firstName"]');
    const lastNameInput = fixture.nativeElement.querySelector('input[formControlName="lastName"]');
    const emailInput = fixture.nativeElement.querySelector('input[formControlName="email"]');
    const passwordInput = fixture.nativeElement.querySelector('input[formControlName="password"]');
    expect(firstNameInput.classList).toContain('ng-invalid');
    expect(lastNameInput.classList).toContain('ng-invalid');
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
