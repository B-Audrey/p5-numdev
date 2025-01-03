import {HttpClient, HttpClientModule} from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionApiService } from './session-api.service';
import {Session} from '../interfaces/session.interface';
import {of} from 'rxjs';
import {cold} from 'jest-marbles';


describe('SessionApiService', () => {

  let mockSessions : Session[] = [
    {
      id: 1,
      name: 'Session 1',
      date: new Date(),
      teacher_id: 1,
      description: 'Description 1',
      users: [1, 2]
    },
    {
      id: 2,
      name: 'Session 2',
      date: new Date(),
      teacher_id: 2,
      description: 'Description 2',
      users: [1, 2, 3]
    }
  ]



  let service: SessionApiService;
  let httpSpy = { get: jest.fn(), delete: jest.fn(), post: jest.fn(), put: jest.fn() };

  beforeEach(() => {

    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ],
      providers: [
        { provide: HttpClient, useValue: httpSpy },
        ]
    });
    service = TestBed.inject(SessionApiService);

    httpSpy.put.mockClear();
    httpSpy.post.mockClear();
    httpSpy.get.mockClear();
    httpSpy.delete.mockClear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call all method and return all sessions', () => {
    httpSpy.get.mockReturnValueOnce(of(mockSessions));
    expect(service.all()).toBeObservable(cold('(a|)', { a: mockSessions }));
    expect(httpSpy.get).toHaveBeenNthCalledWith(1, 'api/session');
  })

  it('should call detail method and return the asked session', () => {
    httpSpy.get.mockReturnValueOnce(of(mockSessions[0]));
    expect(service.detail('1')).toBeObservable(cold('(a|)', { a: mockSessions[0] }));
    expect(httpSpy.get).toHaveBeenNthCalledWith(1, 'api/session/1');
  })

  it('should call delete method and return nothing', () => {
    httpSpy.delete.mockReturnValueOnce(of(null));
    expect(service.delete('1')).toBeObservable(cold('(a|)', { a: null }));
    expect(httpSpy.delete).toHaveBeenNthCalledWith(1, 'api/session/1');
  });

  it('should call create method and return a session', () => {
    const newSession = { ...mockSessions[0], id : 3 };
    httpSpy.post.mockReturnValueOnce(of(newSession));
    expect(service.create(mockSessions[0])).toBeObservable(cold('(a|)', { a: newSession }));
    expect(httpSpy.post).toHaveBeenNthCalledWith(1, 'api/session', mockSessions[0]);
  })

  it('should call update method and return a session', () => {
    const updatedSession = { ...mockSessions[0], description : 'New description' };
    httpSpy.put.mockReturnValueOnce(of(updatedSession));
    expect(service.update('1', updatedSession)).toBeObservable(cold('(a|)', { a: updatedSession }));
    expect(httpSpy.put).toHaveBeenNthCalledWith(1, 'api/session/1', updatedSession);
  })

  it('should call participate method and return nothing', () => {
    httpSpy.post.mockReturnValueOnce(of(null));
    expect(service.participate('1', '1')).toBeObservable(cold('(a|)', { a: null }));
    expect(httpSpy.post).toHaveBeenNthCalledWith(1, 'api/session/1/participate/1', null);
  })

  it('should call unParticipate method and return nothing', () => {
    httpSpy.delete.mockReturnValueOnce(of(null));
    expect(service.unParticipate('1', '1')).toBeObservable(cold('(a|)', { a: null }));
    expect(httpSpy.delete).toHaveBeenNthCalledWith(1, 'api/session/1/participate/1');
  })

});






