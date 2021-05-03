import { Component, OnInit } from '@angular/core';


export interface PeriodicElement {
  name: string;
  weight: number;
}

const ELEMENT_DATA: PeriodicElement[] = [
  { name: 'Madera', weight: 1},
  { name: 'Lana', weight: 4},
  { name: 'Cereales', weight: 6},
  { name: 'Arcilla', weight: 9},
  { name: 'Mineral', weight: 10},
 
];

@Component({
  selector: 'app-stats',
  templateUrl: './stats.component.html',
  styleUrls: ['./stats.component.css']
})
export class StatsComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }
  displayedColumns: string[] = [ 'name', 'weight'];
  dataSource = ELEMENT_DATA;
}
