import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http'
import {
  MatFormFieldModule, MatInputModule, MatAutocompleteModule,
  MatOptionModule
} from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ResultService } from './result.service';
import { NgxSpinnerModule } from 'ngx-spinner';


@NgModule({
  declarations: [
    AppComponent,
  ],

  imports:
    [
      HttpClientModule,NgxSpinnerModule,
      ReactiveFormsModule, MatFormFieldModule, MatInputModule,
      MatAutocompleteModule, MatOptionModule, BrowserAnimationsModule,
      BrowserModule, FormsModule
    ],

  providers: [AppComponent, ResultService],
  bootstrap: [AppComponent]
})

export class AppModule { }
