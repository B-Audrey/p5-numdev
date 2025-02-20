// cypress/integration/session_form.spec.js

import { userConnect } from '../support/utils/get-connection';

describe('SessionEditForm', () => {
  beforeEach(() => {
    userConnect(true);

    cy.intercept('GET', '/api/session/8', (req) => {
      req.reply({ statusCode: 200, fixture: 'detail.json' });
    }).as('getDetail');

    cy.intercept('GET', '/api/teacher', (req) => {
      req.reply({ statusCode: 200, fixture: 'teachers-list.json' });
    }).as('getTeachers');

    cy.intercept('PUT', 'api/session/8', (req) => {
      req.reply({ statusCode: 200 });
    }).as('update');

  });

  it('should display sessions', () => {
    cy.get('mat-card.item').should('have.length', 2);
    cy.get('mat-card.item .mat-card-title').first().should('contain', 'session8');
    cy.get('mat-card.item .mat-card-subtitle').first().should('contain', 'Session on January 1, 2025');
    cy.get('mat-card.item .picture').first().should('have.attr', 'src');
    cy.get('mat-card.item').first().should('contain', 'Here We Go!');
    cy.get('mat-card.item mat-icon').first().should('contain', 'search');
  });

  it('should display Edit button', () => {
    cy.get('mat-card.item button:nth-child(2)').first().should('contain', 'Edit');
  });

  it('should navigate to session edit form', () => {
    cy.get('mat-card.item button:nth-child(2)').first().click();
    cy.url().should('include', '/sessions/update/8');
  });

  it('should display the back button', () => {
    cy.get('mat-card.item button:nth-child(2)').first().click();
    cy.get('mat-card-title button[mat-icon-button]').should('exist');
    cy.get('mat-card-title button[mat-icon-button] mat-icon').should('contain', 'arrow_back');
    cy.get('mat-card-title button[mat-icon-button]').click();
    cy.url().should('include', '/sessions');
  });

  it('should display the form as current logged user is admin', () => {
    cy.get('mat-card.item button:nth-child(2)').first().click();
    cy.get('mat-card-title h1').should('contain', 'Update session');
    cy.get('form').should('exist');
  });

  it('should display the form fields', () => {
    cy.get('mat-card.item button:nth-child(2)').first().click();
    cy.get('mat-form-field').should('have.length', 4);
    cy.get('mat-form-field:first-child input').should('have.attr', 'formControlName', 'name');
    cy.get('mat-form-field:first-child input').should('have.value', 'session8');
    cy.get('mat-form-field:nth-child(2) input').should('have.attr', 'formControlName', 'date');
    cy.get('mat-form-field:nth-child(2) input').should('have.value', '2025-01-01');
    cy.get('mat-form-field:nth-child(3) mat-select').should('have.attr', 'formControlName', 'teacher_id');
    cy.get('mat-form-field textarea').should('have.attr', 'formControlName', 'description');
    cy.get('mat-form-field textarea').should('have.value', 'Here We Go!');
  });

  it('should submit the form and update an existing session', () => {
    cy.get('mat-card.item button:nth-child(2)').first().click();
    cy.get('mat-card-title h1').should('contain', 'Update session');
    cy.get('input[formControlName="name"]').clear().type('Updated Session');
    cy.get('input[formControlName="date"]').clear().type('2025-01-02');
    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('mat-option').contains('Koopa').click();
    cy.get('textarea[formControlName="description"]').clear().type('This is an updated session.');
    cy.get('button[type="submit"]').click();
    cy.url().should('include', '/sessions');
    cy.get('.mat-snack-bar-container').should('contain', 'Session updated !');
  });

});
