import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RouterModule, Routes} from '@angular/router';
import {ToastrModule} from 'ngx-toastr';
import {ModalsService} from './core/services/modals.service';
import {ToastrFactory} from './core/services/toastr/toastr.factory';
import {HttpClientModule} from '@angular/common/http';
import {HomeComponent} from './home/home.component';
import {NavbarComponent} from './navbar/navbar.component';
import {CommonModule} from '@angular/common';
import {InfoModalComponent} from './core/components/info-modal/info-modal.component';
import {FileUploadComponent} from './core/components/file-upload/file-upload.component';

const appRoutes: Routes = [
  {
    path: '',
    component: AppComponent,
    children: [

      {
        path: 'home', component: HomeComponent
      },

      {
        path: '', redirectTo: '/home', pathMatch: 'full'
      }
    ]
  }
];


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomeComponent,
    InfoModalComponent,
    FileUploadComponent
  ],
  imports: [
    BrowserAnimationsModule,
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes),
    NgbModule,
    ToastrModule.forRoot(),
    CommonModule
  ],
  providers: [
    ModalsService,
    ToastrFactory
  ],
  entryComponents: [
    InfoModalComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
