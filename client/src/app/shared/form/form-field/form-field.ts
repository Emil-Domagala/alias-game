import {Component, input, Signal} from '@angular/core';
import {WithField} from '@angular/forms/signals';

@Component({
  selector: 'app-form-field',
  imports: [],
  templateUrl: './form-field.html',
  styleUrl: './form-field.scss',
})
export class FormField {

  label = input.required<string>();
  hint = input<string>();
  error = input<WithField<any>>();
  touched = input.required<Signal<boolean>>()
  valid = input.required<Signal<boolean>>()
  forId = input.required<string>();

}
