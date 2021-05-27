import { Component, OnInit } from '@angular/core';
import { LangService } from 'src/app/service/lang/lang.service';
import { UserService } from 'src/app/service/user/user.service';

@Component({
  selector: 'app-change-language',
  templateUrl: './change-language.component.html',
  styleUrls: ['./change-language.component.sass']
})
export class ChangeLanguageComponent implements OnInit {

  datos;
  lenguajeActual: String  = '0'; // Iniciamos
  lenguajeSeleccionado: String ;
  espanyolLanguage: String;
  inglesLanguage: String;
  

  constructor(public userService: UserService, public langService: LangService) { 
   this.datos = ["Español", "Ingles"]
  }

  ngOnInit(): void {
    if(this.langService.selectedLang == "ESP"){
      this.espanyolLanguage = "Español";
      this.inglesLanguage = "Inglés";
    }else if(this.langService.selectedLang == "ENG"){
      this.espanyolLanguage = "Spanish";
      this.inglesLanguage = "English";
    }
  }

  capturar() {
    if(this.lenguajeActual == "1"){
      this.espanyolLanguage = "Spanish";
      this.inglesLanguage = "English";
      this.langService.setSelectedLang("ENG");
      this.userService.updateIdioma("English");
    }else{
      this.espanyolLanguage = "Español";
      this.inglesLanguage = "Inglés";
      this.langService.setSelectedLang("ESP");
      this.userService.updateIdioma("Español");
    }
  }
}
