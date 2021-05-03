import { NgModule,CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginRoutingModule } from './login-routing.module';
import { SigninComponent } from './components/signin/signin.component';
import { RegisterComponent } from './components/register/register.component';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { AngularMaterialModule } from './login-material.module';
import { FormsModule } from '@angular/forms';
import { MainLoginComponent } from './components/main-login/main-login.component';
import { MatDialogModule } from '@angular/material/dialog';

@NgModule({
  declarations: [SigninComponent, RegisterComponent, ResetPasswordComponent, MainLoginComponent],
  imports: [
    CommonModule,
    LoginRoutingModule,
    FlexLayoutModule,
    AngularMaterialModule,
    FormsModule,
    MatDialogModule
  ],
  exports: [
    SigninComponent, 
    RegisterComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LoginModule { }
