/**
 * @type {Cypress.PluginConfig}
 */
 // @ts-ignore
import * as registerCodeCoverageTasks from '@cypress/code-coverage/task';

 export default (on:any, config:any) => {
   return registerCodeCoverageTasks(on, config);
 };
