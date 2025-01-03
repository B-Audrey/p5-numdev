import {HttpClient, HttpClientModule} from '@angular/common/http';
import {TestBed} from '@angular/core/testing';
import {expect} from '@jest/globals';
import {TeacherService} from './teacher.service';
import {of} from 'rxjs';
import {cold} from 'jest-marbles';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpSpy = {get: jest.fn()};
  let mockTeachers = [
    {
      id: 1,
      firstName: 'firstName',
      lastName: 'lastName',
      email: 'teacher@dev.fr',
    },
    {
      id: 2,
      firstName: 'firstName',
      lastName: 'lastName',
      email: 'teacher2@dev.fr',
    }];
  beforeEach(() => {

    TestBed.configureTestingModule({
      imports: [
        HttpClientModule
      ],
      providers: [
        {provide: HttpClient, useValue: httpSpy},
      ]
    });
    service = TestBed.inject(TeacherService);

    httpSpy.get.mockClear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call all and return a list of teachers', () => {
    httpSpy.get.mockReturnValueOnce(of(mockTeachers));
    expect(service.all()).toBeObservable(cold('(a|)', {a: mockTeachers}));
    expect(httpSpy.get).toHaveBeenNthCalledWith(1, 'api/teacher');
  });

  it('should call detail and return a teacher', () => {
    httpSpy.get.mockReturnValueOnce(of(mockTeachers[0]));
    expect(service.detail('1')).toBeObservable(cold('(a|)', {a: mockTeachers[0]}));
    expect(httpSpy.get).toHaveBeenNthCalledWith(1, 'api/teacher/1');
  });

});
