describe('Register Page', () => {
  beforeEach(() => {
    cy.intercept('POST', '/api/auth/register', (req) => {
      if (req.body.firstName === "toto" && req.body.lastName === "titi" && req.body.email === 'toto@test.com' && req.body.password === 'password123') {
        req.reply({statusCode: 200, fixture: 'register-success.json'});
      } else {
        req.reply({statusCode: 400, fixture: 'register-failure.json'});
      }
    }).as('registerRequest');

    cy.visit('/register');
  });

  it('Affiche le formulaire d\'inscription', () => {
    cy.get('mat-card-title').should('contain', 'Register');
    cy.get('input[formControlName="email"]').should('be.visible');
    cy.get('input[formControlName="email"]').should('contain.value', '');
    cy.get('input[formControlName="password"]').should('be.visible');
    cy.get('input[formControlName="password"]').should('contain.value', '');
    cy.get('input[formControlName="firstName"]').should('be.visible');
    cy.get('input[formControlName="firstName"]').should('contain.value', '');
    cy.get('input[formControlName="lastName"]').should('be.visible');
    cy.get('input[formControlName="lastName"]').should('contain.value', '');
    cy.get('form').should('be.visible');
    cy.get('button[type="submit"]').should('be.visible');
    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('Désactive le bouton de connexion si le formulaire est invalide', () => {
    cy.get('input[formControlName="email"]').type('test');
    cy.get('input[formControlName="password"]').type('a');
    cy.get('input[formControlName="firstName"]').type('a');
    cy.get('input[formControlName="lastName"]').type('a');
    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('Permet de s\'inscrire avec des informations valides', () => {
    cy.get('input[formControlName="email"]').type('toto@test.com');
    cy.get('input[formControlName="password"]').type('password123');
    cy.get('input[formControlName="firstName"]').type('toto');
    cy.get('input[formControlName="lastName"]').type('titi');

    cy.get('button[type="submit"]').should('not.be.disabled').click();

    cy.wait('@registerRequest');
    cy.url().should('include', '/login');
  });

  it('Affiche un message d\'erreur en cas d\'échec d\'inscription pour cause de doublon de mail', () => {
    cy.get('input[formControlName="email"]').type('double@test.com');
    cy.get('input[formControlName="password"]').type('wrongpassword');
    cy.get('input[formControlName="firstName"]').type('any');
    cy.get('input[formControlName="lastName"]').type('any');
    cy.get('button[type="submit"]').click();

    cy.wait('@registerRequest');
    cy.get('.error').should('contain', 'An error occurred');
  });

});
