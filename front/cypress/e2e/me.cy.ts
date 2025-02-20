import {userConnect} from '../support/utils/get-connection';

describe('MePage', () => {
  beforeEach(() => {
    userConnect(false);

    cy.intercept('GET', `/api/user/1`, (req) => {
      req.reply({statusCode: 200, fixture: 'user.json'});
    }).as('getUserRequest');

    cy.intercept('DELETE', `/api/user/1`, (req) => {
      req.reply({statusCode: 200});
    }).as('deleteUserRequest');

    cy.get('span[routerLink="me"]').click();

    cy.wait('@getUserRequest');
  });


  it('Affiche les informations utilisateur correctement', () => {
    cy.get('mat-card-title').should('contain', 'User information');
    cy.get('p').should('contain', 'Name: ');
    cy.get('p').should('contain', 'Email: user@test.com');
    cy.get('p').should('contain', 'Create at:');
    cy.get('p').should('contain', 'Last update:');
  });

  it('Affiche le bouton de suppression si l\'utilisateur n\'est pas admin', () => {
    cy.get('button[mat-raised-button').contains('Detail').should('be.visible');
  });


  it('Supprime le compte et redirige vers la page d\'accueil', () => {
    cy.get('button[mat-raised-button').contains('Detail').click();
    cy.get('.mat-snack-bar-container').should('contain', 'Your account has been deleted !');
    cy.url().should('include', '/');
  });

  it('Retourne à la page précédente en cliquant sur le bouton retour', () => {
    cy.go('forward');
    cy.get('button[mat-icon-button]').click();
    cy.go('back');
  });
});
