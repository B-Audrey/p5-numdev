describe('Login Page', () => {
  beforeEach(() => {
    // Mock de l'API login avec les fixtures
    cy.intercept('POST', '/api/auth/login', (req) => {
      if (req.body.email === 'user@test.com' && req.body.password === 'password123') {
        req.reply({ statusCode: 200, fixture: 'login-success.json' });
      } else {
        req.reply({ statusCode: 401, fixture: 'login-failure.json' });
      }
    }).as('loginRequest');

    // Visiter la page de login
    cy.visit('/login');
  });

  it('Affiche le formulaire de connexion', () => {
    cy.get('mat-card-title').should('contain', 'Login');
    cy.get('input[formControlName="email"]').should('be.visible');
    cy.get('input[formControlName="password"]').should('be.visible');
    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('Désactive le bouton de connexion si le formulaire est invalide', () => {
    cy.get('input[formControlName="email"]').type('test');
    cy.get('input[formControlName="password"]').type('a');
    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('Permet de se connecter avec des identifiants valides', () => {
    cy.get('input[formControlName="email"]').type('user@test.com');
    cy.get('input[formControlName="password"]').type('password123');
    cy.get('button[type="submit"]').should('not.be.disabled').click();

    cy.wait('@loginRequest');
    cy.url().should('include', '/sessions');
  });

  it('Affiche un message d\'erreur en cas d\'échec de connexion', () => {
    cy.get('input[formControlName="email"]').type('wrong@test.com');
    cy.get('input[formControlName="password"]').type('wrongpassword');
    cy.get('button[type="submit"]').click();

    cy.wait('@loginRequest');
    cy.get('.error').should('contain', 'An error occurred');
  });
});
