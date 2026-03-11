import {Component, inject, input, Input, OnChanges} from '@angular/core';
import { FilterConfig, QueryFilter} from '../../query-config-model.model';
import { QueryStateService} from '../../query-state.service';
import {FilterFieldComponent} from './filter-field.component/filter-field.component';

@Component({
  selector: 'app-query-filters',
  standalone: true,
  imports: [
    FilterFieldComponent
  ],
  templateUrl: './query-filters.component.html',
  styleUrl: './query-filters.component.scss',
})
export class QueryFiltersComponent implements OnChanges{

  filters = input.required<FilterConfig[]>()
  private state = inject(QueryStateService)

  ngOnChanges(): void {
    console.log('filters changed:', this.filters());
  }

  updateFilter(event: { field: string, filter: QueryFilter | null }) {

    const current = this.state.filters();

    if (!event.filter) {
      this.state.setFilters(current.filter(f => f.field !== event.field));
      return;
    }

    const idx = current.findIndex(f => f.field === event.field);

    if (idx >= 0) {
      current[idx] = event.filter;
      this.state.setFilters([...current]);
    } else {
      this.state.setFilters([...current, event.filter]);
    }

  }

}
