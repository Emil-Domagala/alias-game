import { Injectable } from '@angular/core';
import {ApiError} from '../../api-error.model';
import {FieldTree, ValidationError} from '@angular/forms/signals';

@Injectable({
  providedIn: 'root',
})
export class ApiErrorMapperService {

  /**
   * Maps backend API validation errors to Angular Signals form validation errors.
   *
   * - If the API provides field-specific errors, they are mapped to corresponding form fields.
   * - If no field errors exist, a general form-level error is returned.
   *
   * @template T Type of the form model
   * @param apiError Error response returned from the API
   * @param form Angular Signals FieldTree representing the form
   * @returns Array of validation errors compatible with Angular Signals forms
   */
  mapApiErrorToValidationErrors<T>(
    apiError: ApiError,
    form: FieldTree<T>
  ): ValidationError.WithOptionalFieldTree[] {

    const errors: ValidationError.WithOptionalFieldTree[] = [];

    if (apiError.errors?.length) {
      apiError.errors.forEach(e => {
        const field = e.field as keyof T;

        errors.push({
          kind: 'server',
          message: e.message,
          fieldTree: (form as any)[field]
        });
      });
    }

    return errors.length
      ? errors
      : [{
        kind: 'server',
        message: apiError.message,
        fieldTree: form
      }];
  }
}
