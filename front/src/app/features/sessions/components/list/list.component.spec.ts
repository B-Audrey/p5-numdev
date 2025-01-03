import {ComponentFixture, TestBed} from '@angular/core/testing';
import {MatCardModule} from '@angular/material/card';
import {MatIconModule} from '@angular/material/icon';
import {expect} from '@jest/globals';
import {SessionService} from 'src/app/services/session.service';
import {ListComponent} from './list.component';
import {MatButtonModule} from '@angular/material/button';
import {RouterTestingModule} from '@angular/router/testing';
import {SessionApiService} from '../../services/session-api.service';
import {By} from '@angular/platform-browser';
import {of} from 'rxjs';
import {Router} from '@angular/router';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let router : any;
  let sessionServiceMock: any;
  let sessionApiServiceMock: any;
  const mockSessions = [
    { id: 1, name: 'Yoga Basics', date: '2024-12-15', description: 'Introduction to yoga.' },
    { id: 2, name: 'Advanced Yoga', date: '2024-12-20', description: 'For experienced practitioners.' }
  ];
  beforeEach(() => {
    sessionServiceMock = {
      sessionInformation: { admin: true }
    };

    sessionApiServiceMock = {
      all: jest.fn().mockReturnValue(of(mockSessions)) // Mock initialisé ici
    };

    TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [
        MatCardModule,
        MatButtonModule,
        MatIconModule,
        RouterTestingModule.withRoutes([
          { path: 'detail/:id', component: ListComponent }, // Route fictive reviens sur soi même
          { path: 'update/:id', component: ListComponent }, // Route fictive
        ]),
      ],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: SessionApiService, useValue: sessionApiServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    jest.spyOn(router, 'navigate'); // Spy sur la méthode navigate
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display the "Create" button for admin users', () => {
    sessionServiceMock.sessionInformation = { admin: true };
    fixture.detectChanges();
    const createButton = fixture.debugElement.query(By.css('button[routerLink="create"]'));
    expect(createButton).toBeTruthy();
  });

  it('should not display the "Create" button for non-admin users', () => {
    sessionServiceMock.sessionInformation = { admin: false };
    fixture.detectChanges();
    const createButton = fixture.debugElement.query(By.css('button[routerLink="create"]'));
    expect(createButton).toBeNull();
  });

  it('should display a list of sessions',() => {
    const sessionElements = fixture.debugElement.queryAll(By.css('.item'));
    expect(sessionElements.length).toBe(2);
    expect(sessionElements[0].nativeElement.textContent).toContain('Yoga Basics');
    expect(sessionElements[1].nativeElement.textContent).toContain('Advanced Yoga');
  });

  it('should display the "Edit" button for admin users', () => {
    const editButton = fixture.debugElement.queryAll(By.css('button'))
      .find(btn => btn.nativeElement.textContent.includes('Edit'));
    expect(editButton).toBeTruthy();
  });

  it('should not display the "Edit" button for non-admin users', () => {
    sessionApiServiceMock.all.mockReturnValue(of(mockSessions));
    sessionServiceMock.sessionInformation = { admin: false };
    fixture.detectChanges();
    const editButton = fixture.debugElement.queryAll(By.css('button'))
      .find(btn => btn.nativeElement.textContent.includes('Edit'));
    expect(editButton).toBeFalsy();
  });

  it('should display the "Detail" button all users', () => {
    const detailButton = fixture.debugElement.queryAll(By.css('button'))
      .find(btn => btn.nativeElement.textContent.includes('Detail'));
    expect(detailButton).toBeTruthy();
  });

  it('should have the correct routerLink for the "Detail" button', () => {
    const detailButton = fixture.debugElement.queryAll(By.css('button'))
      .find(btn => btn.nativeElement.textContent.includes('Detail'));
    expect(detailButton).toBeTruthy();
    const routerLink = detailButton?.attributes['ng-reflect-router-link'];
    expect(routerLink).toContain('detail,1');
  });

  it('should have the correct routerLink for the "Edit" button', () => {
    const editButton = fixture.debugElement.queryAll(By.css('button'))
      .find(btn => btn.nativeElement.textContent.includes('Edit'));
    expect(editButton).toBeTruthy();
    const routerLink = editButton?.attributes['ng-reflect-router-link'];
    expect(routerLink).toContain('update,1');
  });

});
