import {HttpClient, HttpClientModule} from '@angular/common/http';
import {TestBed} from '@angular/core/testing';
import {expect} from '@jest/globals';
import {UserService} from './user.service';
import {of} from 'rxjs';
import {cold} from 'jest-marbles';

describe('UserService', () => {
  let service: UserService;
  let httpSpy = {get: jest.fn(), delete: jest.fn()};

  beforeEach(() => {

    TestBed.configureTestingModule({
      imports: [
        HttpClientModule
      ],
      providers: [
        {provide: HttpClient, useValue: httpSpy},
      ]
    });
    service = TestBed.inject(UserService);

    httpSpy.get.mockClear();
    httpSpy.delete.mockClear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call user by id and return a user', () => {
    let mockUser = {
      id: 1,
      firstName: 'Peach',
      lastName: 'Princess',
      email: 'email@dev.fr',
    }
    httpSpy.get.mockReturnValueOnce(of(mockUser));
    expect(service.getById('1')).toBeObservable(cold('(a|)', {a: mockUser}));
    expect(httpSpy.get).toHaveBeenNthCalledWith(1, 'api/user/1');
  });

  it('should call delete user by id', () => {
    httpSpy.delete.mockReturnValueOnce(of({}));
    expect(service.delete('1')).toBeObservable(cold('(a|)', {a: {}}));
    expect(httpSpy.delete).toHaveBeenNthCalledWith(1, 'api/user/1');
  });

});
