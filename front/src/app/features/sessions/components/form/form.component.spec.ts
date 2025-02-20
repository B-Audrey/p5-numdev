import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatSelect, MatSelectModule} from '@angular/material/select';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {RouterTestingModule} from '@angular/router/testing';
import {expect} from '@jest/globals';
import {SessionService} from 'src/app/services/session.service';
import {SessionApiService} from '../../services/session-api.service';
import {FormComponent} from './form.component';
import {of} from 'rxjs';
import {ActivatedRoute, Router} from '@angular/router';
import {TeacherService} from '../../../../services/teacher.service';
import {OverlayContainer} from '@angular/cdk/overlay';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let overlayContainer: OverlayContainer;
  let overlayContainerElement: HTMLElement;
  let mockYogaValue = {
    name: 'New Session',
    date: '2023-01-01',
    teacher_id: '1',
    description: 'New Description',
  };
  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }
  let mockRouter: any;
  let mockActivatedRoute: any;
  let mockMatSnackBar: any;
  let mockTeacherService: any;
  let mockSessionApiService: any;
  let mockMatSelect: any;

  beforeEach(async () => {
    mockRouter = {navigate: jest.fn(), url: ''};
    mockMatSnackBar = {open: jest.fn()};
    mockTeacherService = {all: jest.fn().mockReturnValue(of([]))};
    mockMatSelect = {open: jest.fn()};
    mockSessionApiService = {
      detail: jest.fn().mockReturnValue(of({})),
      create: jest.fn().mockReturnValue(of({})),
      update: jest.fn().mockReturnValue(of({})),
    };
    mockActivatedRoute = {snapshot: {paramMap: {get: jest.fn().mockReturnValue('')}}};


    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,NoopAnimationsModule
      ],
      providers:
        [{provide: SessionService, useValue: mockSessionService},
          {provide: Router, useValue: mockRouter},
          {provide: ActivatedRoute, useValue: mockActivatedRoute},
          {provide: MatSnackBar, useValue: mockMatSnackBar},
          {provide: TeacherService, useValue: mockTeacherService},
          {provide: SessionApiService, useValue: mockSessionApiService},
          {provide : MatSelect, useValue: MatSelect},
          OverlayContainer
        ],
      declarations: [FormComponent]
    })
      .compileComponents();


    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    overlayContainer = TestBed.inject(OverlayContainer);
    overlayContainerElement = overlayContainer.getContainerElement();
  });
  beforeEach(() => {
    overlayContainer = TestBed.inject(OverlayContainer);
    overlayContainerElement = overlayContainer.getContainerElement();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect non-admin users to /sessions', () => {
    mockSessionService.sessionInformation.admin = false;
    const navigateSpy = jest.spyOn(mockRouter, 'navigate');
    component.ngOnInit();
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });


  it('should call teacherService.all and return teachers', () => {
    const mockTeachers = [
      { id: '1', firstName: 'Luigi', lastName: 'BROS' },
      { id: '2', firstName: 'Baby', lastName: 'BOWSER' },
      { id: '3', firstName: 'Big', lastName: 'BOWSER' },
    ];
    const allSpy = jest.spyOn(mockTeacherService, 'all').mockReturnValue(of(mockTeachers));
    fixture.detectChanges();
    expect(allSpy).toHaveBeenCalled();
    mockTeacherService.all().subscribe((teachers:any) => {
      expect(teachers).toEqual(mockTeachers);
    });
  });

  it('should initialize form with default values if no session is provided', () => {
    component.ngOnInit();
    expect(component.sessionForm?.value).toEqual({
      name: '',
      date: '',
      teacher_id: '',
      description: '',
    });
  });

  it('should initialize form with session details for update', () => {
    mockRouter.url = '/update/1';
    jest.spyOn(mockSessionApiService, 'detail').mockReturnValue(of(mockYogaValue));

    component.ngOnInit();

    expect(component.sessionForm?.value).toEqual({
      name: mockYogaValue.name,
      date: mockYogaValue.date,
      teacher_id: mockYogaValue.teacher_id,
      description: mockYogaValue.description,
    });
  });

  it('should call make API call when submitting a new session', () => {
    const mockFormValue = {
      name: 'New Session',
      date: '2023-01-01',
      teacher_id: '1',
      description: 'New Description',
    };

    // Initialise le formulaire avec les valeurs simulées
    component.sessionForm?.setValue(mockFormValue);

    // Espionne les appels à l'API et à la méthode exitPage
    const createSpy = jest.spyOn(mockSessionApiService, 'create').mockReturnValue(of({}));
    const exitPageSpy = jest.spyOn<any, string>(component, 'exitPage');

    // Appelle la méthode submit
    component.submit();

    // Vérifie que l'API et la méthode exitPage sont bien appelées avec les bons paramètres
    expect(createSpy).toHaveBeenCalledWith(mockFormValue);
    expect(exitPageSpy).toHaveBeenCalledWith('Session created !');
  });

  it('should call update API when submitting an existing session', () => {
    // Simule un mode "update" et un ID existant
    component.onUpdate = true;
    component['id'] = '1'; // Accès à la propriété privée via indexation

    const mockFormValue = {
      name: 'Updated Session',
      date: '2023-01-01',
      teacher_id: '1',
      description: 'Updated Description',
    };

    // Initialise le formulaire avec les valeurs simulées
    component.sessionForm?.setValue(mockFormValue);

    // Espionne les appels à l'API et à la méthode exitPage
    const updateSpy = jest.spyOn(mockSessionApiService, 'update').mockReturnValue(of({}));
    const exitPageSpy = jest.spyOn<any, string>(component, 'exitPage');

    // Appelle la méthode submit
    component.submit();

    // Vérifie que l'API et la méthode exitPage sont bien appelées avec les bons paramètres
    expect(updateSpy).toHaveBeenCalledWith('1', mockFormValue);
    expect(exitPageSpy).toHaveBeenCalledWith('Session updated !');
  });


  it('should display a snackbar and navigate to sessions on exitPage', () => {
    const message = 'Test Message';
    component['exitPage'](message);

    expect(mockMatSnackBar.open).toHaveBeenCalledWith(message, 'Close', {duration: 3000});
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

});
