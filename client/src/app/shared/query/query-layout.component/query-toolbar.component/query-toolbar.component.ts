import { Component, inject, input, OnDestroy } from '@angular/core';
import { QueryConfig } from '../../query-config-model.model';
import { QueryStateService } from '../../query-state.service';

@Component({
  selector: 'app-query-toolbar',
  standalone: true,
  templateUrl: './query-toolbar.component.html',
  styleUrls: ['./query-toolbar.component.scss'],
})
export class QueryToolbarComponent implements OnDestroy {

  config = input.required<QueryConfig>();
  state = inject(QueryStateService);

  private searchTimeout?: ReturnType<typeof setTimeout>;

  search(e: Event) {
    const value = (e.target as HTMLInputElement).value;

    if (this.searchTimeout) {
      clearTimeout(this.searchTimeout);
    }

    this.searchTimeout = setTimeout(() => {
      this.state.setSearch(value);
    }, 300);
  }

  sort(e: Event) {
    const value = (e.target as HTMLSelectElement).value;
    this.state.setSort(value);
  }

  toggleDirection() {
    this.state.toggleDirection();
  }

  changePageSize(e: Event) {
    const value = Number((e.target as HTMLSelectElement).value);
    this.state.pageSize.set(value);
  }

  ngOnDestroy() {
    if (this.searchTimeout) {
      clearTimeout(this.searchTimeout);
    }
  }
}
