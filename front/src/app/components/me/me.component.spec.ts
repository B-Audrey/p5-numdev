import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {SessionService} from 'src/app/services/session.service';
import {MeComponent} from './me.component';
import {UserService} from '../../services/user.service';
import {of} from 'rxjs';
import {expect} from '@jest/globals';
import {By} from '@angular/platform-browser';
import {User} from '../../interfaces/user.interface';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockUser: Partial<User> = {
    id: 1,
    firstName: 'John',
    lastName: 'Doe',
    email: 'john.doe@example.com',
    admin: true,
  };

  const mockSessionService = {
    isLogged: true,
    sessionInformation: {
      admin: true,
      id: 1,
    },
    logOut: jest.fn(),
  };

  const mockUserService = {
    getById: jest.fn().mockReturnValue(of(mockUser)),
    delete: jest.fn(),
  };

  const mockMatSnackBar = {
    open: jest.fn(),
  };


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
      providers: [
        {provide: SessionService, useValue: mockSessionService},
        {provide: UserService, useValue: mockUserService},
        { provide: MatSnackBar, useValue: mockMatSnackBar },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch user data on ngOnInit', () => {
    component.ngOnInit();
    fixture.detectChanges();
    expect(mockUserService.getById).toHaveBeenCalledWith('1');
    expect(component.user?.firstName).toEqual(mockUser.firstName);
    expect(component.user?.lastName).toEqual(mockUser.lastName);
    expect(component.user?.email).toEqual(mockUser.email);
    expect(component.user?.admin).toEqual(mockUser.admin);
    expect(component.user?.id).toEqual(1);
  });

  it('should display user information in the template', () => {
    const nameElement = fixture.debugElement.query(By.css('p')).nativeElement;
    expect(nameElement.textContent).toContain('John DOE'); // Nom avec uppercase
  });

  it('should not display delete button if user is admin', () => {
    // @ts-ignore
    component.user.admin = true
    const deleteButton = fixture.debugElement.query(By.css('button[color="warn"]'));
    expect(deleteButton).toBeNull();
  });

  it('should display delete button only if user is not admin', () => {
    // @ts-ignore
    component.user.admin = false
    fixture.detectChanges();
    const deleteButton = fixture.debugElement.query(By.css('button[color="warn"]'));
    expect(deleteButton).not.toBeNull(); // Le bouton doit être présent et différent de null
  });

  it('should delete the user and log out at same time', () => {
    const logOutSpy = jest.spyOn(mockSessionService, 'logOut');
    const snackBarSpy = jest.spyOn(mockMatSnackBar, 'open');
    const routerSpy = jest.spyOn(component['router'], 'navigate');
    mockUserService.delete.mockReturnValue(of(null));
    component.delete();
    fixture.detectChanges();
    expect(mockUserService.delete).toHaveBeenCalledWith('1');
    expect(logOutSpy).toHaveBeenCalled();
    expect(routerSpy).toHaveBeenCalledWith(['/']);
    expect(snackBarSpy).toHaveBeenCalledWith(
      'Your account has been deleted !',
      'Close',
      {duration: 3000}
    );
  });

});
