import { Component, input } from '@angular/core';
import { FieldState, MaybeFieldTree } from '@angular/forms/signals';

@Component({
  selector: 'app-form-field',
  templateUrl: './form-field.component.html',
  styleUrls: ['./form-field.component.scss'],
})
export class FormFieldComponent<T = any> {
  label = input.required<string>();
  hint = input<string>();
  field = input.required<MaybeFieldTree<T>>();
  forId = input.required<string>();

  /**
   * Safely normalize the field to a FieldState.
   * Field is required, so we always expect something here.
   */
  get control(): FieldState<T> {
    const f = this.field();
    if (!f) {
      throw new Error('[FormFieldComponent] field() returned undefined');
    }
    return typeof f === 'function' ? f() : f;
  }

  get touched(): boolean {
    return this.control.touched();
  }

  get valid(): boolean {
    return this.control.valid();
  }

  get errorMessage(): string | null {
    const errors = this.control.errors();
    if (!errors || this.valid) return null;
    return Object.values(errors)[0]?.message ?? null;
  }
}
