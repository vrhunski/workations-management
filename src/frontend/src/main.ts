import 'zone.js'; // ✅ Required for Zone.js-based change detection

import {bootstrapApplication} from '@angular/platform-browser';
import {App} from './app/app';
import {provideRouter} from '@angular/router';
import {routes} from './app/app.routes';
import {HttpClientModule} from '@angular/common/http';
import {importProvidersFrom} from '@angular/core';


bootstrapApplication(App, /*appConfig*/
  {
    providers: [
      importProvidersFrom(HttpClientModule),
      provideRouter(routes)
    ],
  }
)
  .catch((err) => console.error(err));
