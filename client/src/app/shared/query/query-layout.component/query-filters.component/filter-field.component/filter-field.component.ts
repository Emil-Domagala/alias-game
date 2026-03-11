import { Component, computed, input, output } from '@angular/core';
import {
  FilterConfig,
  FilterOperator,
  QueryFilter,
  FilterType
} from '../../../query-config-model.model';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-filter-field',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './filter-field.component.html',
  styleUrls: ['./filter-field.component.scss']
})
export class FilterFieldComponent {

  config = input.required<FilterConfig>();

  filterChange = output<{ field: string; filter: QueryFilter | null }>();

  value = '';
  operator: FilterOperator = FilterOperator.EQ;

  FilterType = FilterType;

  operatorLabels: Record<FilterOperator, string> = {
    EQ: 'Equals',
    CONTAINS: 'Contains',
    LT: 'Less than',
    GT: 'Greater than',
    IN: 'In'
  };

  label = computed(() =>
    this.config().field
      .replace(/([A-Z])/g, ' $1')
      .replace(/^./, s => s.toUpperCase())
  );

  apply() {

    const field = this.config().field;

    if (!this.value) {
      this.filterChange.emit({
        field,
        filter: null
      });
      return;
    }

    this.filterChange.emit({
      field,
      filter: {
        field,
        operator: this.operator,
        value: this.value
      }
    });

  }

}
