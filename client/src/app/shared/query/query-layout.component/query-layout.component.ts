import {Component, inject, input} from '@angular/core';
import {config} from 'rxjs';
import {QueryConfig} from '../query-config-model.model';
import {QueryFiltersComponent} from './query-filters.component/query-filters.component';
import {QueryToolbarComponent} from './query-toolbar.component/query-toolbar.component';

@Component({
  selector: 'app-query-layout',
  imports: [
    QueryFiltersComponent,
    QueryToolbarComponent
  ],
  templateUrl: './query-layout.component.html',
  styleUrl: './query-layout.component.scss',
})
export class QueryLayoutComponent {
  config = input.required<QueryConfig>();

}
