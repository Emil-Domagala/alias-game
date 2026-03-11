import { Injectable, signal } from '@angular/core';
import { QueryFilter } from './query-config-model.model';

@Injectable({
  providedIn: 'root'
})
export class QueryStateService {
  search = signal<string>('');
  filters = signal<QueryFilter[]>([]);
  sortField = signal<string>('');
  direction = signal<'ASC' | 'DESC'>('ASC');

  pageSize = signal<number>(10);

  setSearch(v: string) {
    this.search.set(v);
  }

  setFilters(filters: QueryFilter[]) {
    this.filters.set(filters);
  }

  setSort(field: string) {
    this.sortField.set(field);
  }

  toggleDirection() {
    this.direction.update(v => v === 'ASC' ? 'DESC' : 'ASC');
  }

}
