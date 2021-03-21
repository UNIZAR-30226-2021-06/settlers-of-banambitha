import { NgModule,CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginRoutingModule } from './login-routing.module';
import { SigninComponent } from './components/signin/signin.component';
import { RegisterComponent } from './components/register/register.component';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { AngularMaterialModule } from './login-material.module';




@NgModule({
  declarations: [SigninComponent, RegisterComponent, ResetPasswordComponent],
  imports: [
    CommonModule,
    LoginRoutingModule,
    FlexLayoutModule,
    AngularMaterialModule
  ],
  exports: [
    SigninComponent, 
    RegisterComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LoginModule { }
