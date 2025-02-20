describe('Not Found Page', () => {
  it('Affiche une page 404 si l\'URL n\'existe pas', () => {
    cy.visit('/unknown');
    cy.get('h1').should('contain', 'Page not found');
  })
});
