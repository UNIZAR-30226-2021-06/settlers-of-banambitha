import { Injectable } from '@angular/core';
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
}
