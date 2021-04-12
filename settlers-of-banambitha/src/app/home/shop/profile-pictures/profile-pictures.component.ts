import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-profile-pictures',
  templateUrl: './profile-pictures.component.html',
  styleUrls: ['./profile-pictures.component.sass']
})
export class ProfilePicturesComponent implements OnInit {

  constructor() { }

  imageNames : string [] = [
    'Dave',
    'Señor bigotes',
    'Científica',
    'Bruno',
    'Agente Secreto',
    'Desarrollador Dae',
    'Developer',
    'Ingeniera',
    'Azafata',
    'Alfred',
    'Profesora',
    'Señora formal',
    'Astronauta',
    'Anciano feliz',
    'Chico tranquilo',
    'barbero',
    'Hombre desconfiado'
  ]

  getRandomInt() {
    return Math.floor(Math.random() * 1000000);
  }

  ngOnInit(): void {
  }

}
