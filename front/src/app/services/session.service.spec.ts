import {TestBed} from '@angular/core/testing';
import {expect} from '@jest/globals';
import {SessionService} from './session.service';
import {HttpClientModule} from '@angular/common/http';
import {SessionInformation} from '../interfaces/sessionInformation.interface';
import {hot} from 'jest-marbles';

describe('SessionService', () => {
  let service: SessionService;
  let mockSessionInformation: SessionInformation = {
    id: 1,
    firstName: 'firstName',
    lastName: 'lastName',
    admin: true,
    token: 'token',
    type: 'type',
    username: 'username',
  };
  beforeEach(() => {

    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ],
    });
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should emit the initial isLogged state as false', () => {
    expect(service.$isLogged()).toBeObservable(hot('(a)', { a: false }));
  });

  it('should update isLogged to true after login', () => {
    service.logIn(mockSessionInformation);
    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(mockSessionInformation);
    expect(service.$isLogged()).toBeObservable(hot('(a)', { a: true}));
  });

  it('should update isLogged to false after logout', () => {
    const isLogged$ = service.$isLogged();
    service.logOut();
    expect(isLogged$).toBeObservable(hot('(a)', { a: false }));
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should call next() when logIn or logOut is called', () => {
    const nextSpy = jest.spyOn(service as any, 'next');

    service.logIn(mockSessionInformation);
    expect(nextSpy).toHaveBeenCalledTimes(1);

    service.logOut();
    expect(nextSpy).toHaveBeenCalledTimes(2);
  });
});
