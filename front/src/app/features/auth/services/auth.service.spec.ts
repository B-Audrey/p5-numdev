import {HttpClient, HttpClientModule} from '@angular/common/http';
import {TestBed} from '@angular/core/testing';
import {expect} from '@jest/globals';
import {SessionInformation} from '../../../interfaces/sessionInformation.interface';
import {AuthService} from './auth.service';
import {of} from 'rxjs';
import {cold} from 'jest-marbles';
import {RegisterRequest} from '../interfaces/registerRequest.interface';
import {LoginRequest} from '../interfaces/loginRequest.interface';


describe('AuthService', () => {
  let mockSessionInformation: SessionInformation = {
    id: 1,
    firstName: 'firstName',
    lastName: 'lastName',
    admin: true,
    token: 'token',
    type: 'type',
    username: 'username',
  };

  let service: AuthService;
  let httpSpy = {post: jest.fn()};

  beforeEach(() => {

    TestBed.configureTestingModule({
      imports: [
        HttpClientModule
      ],
      providers: [
        {provide: HttpClient, useValue: httpSpy},
      ]
    });
    service = TestBed.inject(AuthService);
  });

  beforeEach(() => {
    httpSpy.post.mockClear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call register and return nothing', () => {
    const newUser: RegisterRequest = {
      email: 'email@dev.fr',
      password: 'password',
      firstName: 'firstName',
      lastName: 'lastName'
    };
    httpSpy.post.mockReturnValueOnce(of(newUser));
    expect(service.register(newUser)).toBeObservable(cold('(a|)', {a: newUser}));
    expect(httpSpy.post).toHaveBeenNthCalledWith(1, 'api/auth/register', newUser);
  });

  it('should call login and return a sessionInformation', () => {
    const loginRequest: LoginRequest = {
      email: 'email@dev.fr',
      password: 'password'
    };
    httpSpy.post.mockReturnValueOnce(of(mockSessionInformation));
    expect(service.login(loginRequest)).toBeObservable(cold('(a|)', {a: mockSessionInformation}));
    expect(httpSpy.post).toHaveBeenNthCalledWith(1, 'api/auth/login', loginRequest);
  });


});






