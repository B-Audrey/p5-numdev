export const userConnect = (isAdmin:boolean) => {
  cy.intercept('POST', '/api/auth/login', (req) => {
    if (!isAdmin) {
      req.reply({statusCode: 200, fixture: 'user-login-success.json'});
    } else {
      req.reply({statusCode: 200,  fixture: 'admin-login-success.json'});
    }
  }).as('loginRequest');
  cy.intercept('GET', '/api/session', (req) => {
    req.reply({statusCode: 200, fixture: 'sessions.json'});
  }).as('getSessions');

  cy.visit('/login');
  cy.get('input[formControlName="email"]').type('user@test.com');
  cy.get('input[formControlName="password"]').type('password123');
  cy.get('button[type="submit"]').click();
  cy.wait('@loginRequest', {timeout: 10000});

  cy.wait('@getSessions', {timeout: 10000});
}
