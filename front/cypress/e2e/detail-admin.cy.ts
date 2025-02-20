import {userConnect} from '../support/utils/get-connection';

describe('DetailPageForAdmin', () => {
  beforeEach(() => {

    cy.intercept('GET', '/api/session/8', (req) => {
      req.reply({statusCode: 200, fixture: 'detail-participating.json'});
    }).as('getDetail');

    cy.intercept('GET', '/api/teacher/1', (req) => {
      req.reply({statusCode: 200, fixture: 'teacher.json'});
    }).as('getTeacher');

    cy.intercept('DELETE', 'api/session/8', (req) => {
      req.reply({statusCode: 200});
    }).as('delete');

    userConnect(true);
    cy.get('mat-card.item button').first().click();
    cy.wait('@getDetail');
    cy.wait('@getTeacher');
  });

  it('should display session details', () => {
    cy.get('h1').should('contain', 'Session8');
    cy.get('mat-card-subtitle span').should('contain', 'King Boo TEACHER');
    cy.get('div[mat-card-image] img').should('have.attr', 'src', 'assets/sessions.png');
    cy.get('mat-card-content div:nth-child(2) span').should('contain', 'January 1, 2025');
    cy.get('mat-card-content .description').should('contain', 'Here We Go!');
  });

  it('should display the back button', () => {
    cy.get('mat-card-title button[mat-icon-button]').should('exist');
    cy.get('mat-card-title button[mat-icon-button] mat-icon').should('contain', 'arrow_back');
    cy.get('mat-card-title button[mat-icon-button]').click();
    cy.url().should('include', '/sessions');
  });

  it('should permit to delete session by button as current logged user is admin', () => {
    cy.get('mat-card-title button[mat-raised-button]').should('contain', 'Delete');
    cy.get('mat-card-title button[mat-raised-button]').click();
    cy.wait('@delete');
    cy.get('.mat-snack-bar-container').should('contain', 'Session deleted !');
    cy.url().should('include', '/sessions');
  });

});
