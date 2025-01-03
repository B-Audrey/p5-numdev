import {ComponentFixture, fakeAsync, TestBed} from '@angular/core/testing';
import {DetailComponent} from './detail.component';
import {MatSnackBar} from '@angular/material/snack-bar';
import {ActivatedRoute, Router} from '@angular/router';
import {of} from 'rxjs';
import {SessionService} from '../../../../services/session.service';
import {SessionApiService} from '../../services/session-api.service';
import {TeacherService} from '../../../../services/teacher.service';
import {MatCardModule} from '@angular/material/card';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {expect} from '@jest/globals';
import {By} from '@angular/platform-browser';
import {DebugElement} from '@angular/core';

// Mock des services
const mockSessionService = {
  sessionInformation: {id: 1, admin: true}
};

let mockSessionApiService = {
  detail: jest.fn().mockReturnValue(of({
    id: 9,
    name: 'Yoga Session',
    teacher_id: 2,
    users: [2, 3, 4],
    date: new Date(),
    createdAt: new Date(),
    updatedAt: new Date(),
    description: 'A relaxing yoga session'
  })),
  delete: jest.fn().mockReturnValue(of({})),
  participate: jest.fn().mockReturnValue(of({})),
  unParticipate: jest.fn().mockReturnValue(of({}))
};

const mockTeacherService = {
  detail: jest.fn().mockReturnValue(of({firstName: 'Luigi', lastName: 'Bros'}))
};
const mockActivatedRoute = {snapshot: {paramMap: {get: jest.fn().mockReturnValue('9')}}};
const mockMatSnackBar = {open: jest.fn()};
const mockRouter = {navigate: jest.fn()};

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DetailComponent],
      imports: [
        MatCardModule,
        MatIconModule,
        MatButtonModule,
        BrowserAnimationsModule,
        FormsModule,
        ReactiveFormsModule
      ],
      providers: [
        {provide: SessionService, useValue: mockSessionService},
        {provide: SessionApiService, useValue: mockSessionApiService},
        {provide: TeacherService, useValue: mockTeacherService},
        {provide: MatSnackBar, useValue: mockMatSnackBar},
        {provide: ActivatedRoute, useValue: mockActivatedRoute},
        {provide: Router, useValue: mockRouter}
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch and display session and teacher data on init', () => {
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('9');
    expect(mockTeacherService.detail).toHaveBeenCalledWith('2');
    fixture.detectChanges();

    expect(component.session?.name).toEqual('Yoga Session');
    expect(component.teacher?.firstName).toEqual('Luigi');
    expect(component.teacher?.lastName).toEqual('Bros');

    const h1Element = fixture.nativeElement.querySelector('h1');
    expect(h1Element.textContent).toContain('Yoga Session');
    const teacherElement = fixture.debugElement.queryAll(By.css('span.ml1'));
    const isTeacherElementFound = teacherElement
      .find((span: DebugElement) => span.nativeElement.textContent === 'Luigi BROS');
    expect(isTeacherElementFound).toBeTruthy();
  });

  it('should call window.history.back on back method', () => {
    const backSpy = jest.spyOn(window.history, 'back');
    component.back();
    expect(backSpy).toHaveBeenCalled();
  });

  it('should (as admin) call delete method and navigate on delete', () => {
    component.delete();
    expect(mockSessionApiService.delete).toHaveBeenCalledWith('9');
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', {duration: 3000});
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should not display delete button if the user is not admin', () => {
    mockSessionService.sessionInformation = {...mockSessionService.sessionInformation, admin: false};
    // force non admin update
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    const deleteButton = fixture.debugElement.queryAll(By.css('button'))
      .find(btn => btn.nativeElement.textContent.includes('Delete'));
    expect(deleteButton).toBeFalsy();
  });
  it('should display participate button if the user is not admin', () => {
    mockSessionService.sessionInformation = {...mockSessionService.sessionInformation, admin: false};
    // force non admin update
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    const participateButton = fixture.debugElement.queryAll(By.css('button'))
      .find(btn => btn.nativeElement.textContent.includes('Participate'));
    expect(participateButton).toBeTruthy();
  });

  it('should call participate method', () => {
    mockSessionService.sessionInformation = {...mockSessionService.sessionInformation, admin: false};
    // force non admin update
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    component.participate();
    expect(mockSessionApiService.participate).toHaveBeenCalledWith('9', '1');
  });

  it('should call participate method and update the button to "Do not participate"', fakeAsync(() => {
      mockSessionService.sessionInformation = {...mockSessionService.sessionInformation, admin: false};
      mockSessionApiService.detail = jest.fn().mockReturnValue(of({
        id: 9,
        name: 'Yoga Session',
        teacher_id: 2,
        users: [1],
        date: new Date(),
        createdAt: new Date(),
        updatedAt: new Date(),
        description: 'A relaxing yoga session'
      }))
      mockSessionApiService.detail()
      fixture = TestBed.createComponent(DetailComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
      const unParticipateButton = fixture.debugElement.queryAll(By.css('button'))
        .find(btn => btn.nativeElement.textContent.includes('Do not participate'));
      expect(unParticipateButton).toBeTruthy();
    })
  );
})

