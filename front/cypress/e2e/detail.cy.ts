import {userConnect} from '../support/utils/get-connection';

describe('DetailPageForUser', () => {

  let _isParticipating = false;

  beforeEach(() => {
    cy.intercept('GET', '/api/session/8', (req) => {
      if (_isParticipating) {
        req.reply({statusCode: 200, fixture: 'detail-participating.json'});
      } else {
        req.reply({statusCode: 200, fixture: 'detail.json'});
      }
    }).as('getDetail');

    cy.intercept('GET', '/api/teacher/1', (req) => {
      req.reply({statusCode: 200, fixture: 'teacher.json'});
    }).as('getTeacher');

    cy.intercept('POST', 'api/session/8/participate/1', (req) => {
      req.reply({statusCode: 200});
    }).as('participate');

    cy.intercept('DELETE', 'api/session/8/participate/1', (req) => {
      req.reply({statusCode: 200});
    }).as('unParticipate');

    userConnect(false);
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
    cy.get('mat-card-content div:first-child span').should('contain', '0 attendees');

  });

  it('should display the back button', () => {
    cy.get('mat-card-title button[mat-icon-button]').should('exist');
    cy.get('mat-card-title button[mat-icon-button] mat-icon').should('contain', 'arrow_back');
  });

  it('should permit to participate by button as current logged user is not participating', () => {
    cy.get('mat-card-title button[mat-raised-button]').should('contain', 'Participate');
    _isParticipating = true;
    cy.get('mat-card-title button[mat-raised-button]').click();
    cy.wait('@participate');
    cy.get('mat-card-title button[mat-raised-button]').should('contain', 'Do not participate');
    cy.get('mat-card-title button[mat-raised-button]').should('have.attr', 'color', 'warn');
    cy.get('mat-card-content div:first-child span').should('contain', '1 attendees');

  });

  it('should permit to unparticipate by button as current logged user is participating', () => {
    _isParticipating = false;
    cy.get('mat-card-title button[mat-raised-button]').click();
    cy.wait('@unParticipate');
    cy.get('mat-card-title button[mat-raised-button]').should('contain', 'Participate');
    cy.get('mat-card-title button[mat-raised-button]').should('have.attr', 'color', 'primary');
    cy.get('mat-card-content div:first-child span').should('contain', '0 attendees');
  });

});
