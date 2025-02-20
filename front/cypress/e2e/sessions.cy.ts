import {userConnect} from '../support/utils/get-connection';

describe('SessionPage', () => {
  beforeEach(() => {
    userConnect(false);
    cy.intercept('GET', '/api/session/8', (req) => {
      req.reply({statusCode: 200, fixture: 'detail.json'});
    }).as('getDetail');
  });


  it('should display sessions', () => {
    cy.get('mat-card.item').should('have.length', 2);
    cy.get('mat-card.item .mat-card-title').first().should('contain', 'session8');
    cy.get('mat-card.item .mat-card-subtitle').first().should('contain', 'Session on January 1, 2025');
    cy.get('mat-card.item .picture').first().should('have.attr', 'src');
    cy.get('mat-card.item').first().should('contain', 'Here We Go!');
    cy.get('mat-card.item mat-icon').first().should('contain', 'search');
  });

  it('should navigate to session details', () => {
    cy.get('mat-card.item button').first().click();
    cy.url().should('include', '/sessions/detail/8');
  });

});

