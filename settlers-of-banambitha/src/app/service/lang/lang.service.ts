import { Injectable } from '@angular/core';
import { Recurso } from '../game/game.service';
import dictionaryJSON from './strings.json';


export enum Language {
  ESP = "ESP",
  ENG = "ENG"
}


@Injectable({
  providedIn: 'root'
})
export class LangService {

  public selectedLang: Language = Language.ESP
  public dictionary: Object 

  constructor() {
    this.dictionary = dictionaryJSON
  }

  public get(key: string): string {
    if ( this.dictionary[key] ){
      return this.dictionary[key][this.selectedLang]
    }else{
      return "FAILED"
    }
  }

  public getResource(recurso: Recurso): string {
    switch(recurso){
      case Recurso.MADERA: 
        return this.dictionary["madera"][this.selectedLang]

      case Recurso.LANA: 
        return this.dictionary["lana"][this.selectedLang]

      case Recurso.CEREAL: 
        return this.dictionary["cereales"][this.selectedLang]

      case Recurso.MINERAL: 
        return this.dictionary["mineral"][this.selectedLang]

      case Recurso.ARCILLA: 
        return this.dictionary["arcilla"][this.selectedLang]
      
      default: 
        return "FAILED"
    }
  }
}
