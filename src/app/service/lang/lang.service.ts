import { Injectable } from '@angular/core';
import { Recurso, TipoTerreno } from '../game/game.service';
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

  public getTipoTerreno(tipo: TipoTerreno): string {
    switch(tipo) {
      case TipoTerreno.BOSQUE: 
        return this.dictionary["bosque"][this.selectedLang]

      case TipoTerreno.PASTO: 
        return this.dictionary["prado"][this.selectedLang]

      case TipoTerreno.SEMBRADO: 
        return this.dictionary["sembrado"][this.selectedLang]

      case TipoTerreno.DESIERTO: 
        return this.dictionary["desierto"][this.selectedLang]

      case TipoTerreno.CERRO: 
        return this.dictionary["cerro"][this.selectedLang]

      case TipoTerreno.MONTANYA: 
        return this.dictionary["mountain"][this.selectedLang]

      default: 
        return this.dictionary["unknown"][this.selectedLang]

    }
  }
}
