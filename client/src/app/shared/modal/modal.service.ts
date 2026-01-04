import { inject, Injectable } from '@angular/core';
import { Dialog, DialogRef } from '@angular/cdk/dialog';

export type ModalSize = 'sm' | 'md' | 'lg' | 'xl';

const MODAL_SIZES: Record<ModalSize, string> = {
  sm: '360px',
  md: '480px',
  lg: '720px',
  xl: '960px',
};


@Injectable({ providedIn: 'root' })
export class ModalService {
  private dialog = inject(Dialog);

  open<TComponent, TData = unknown, TResult = unknown>(
    component: TComponent,
    options?: {
      data?: TData;
      size?: ModalSize;
      disableClose?: boolean;
    }
  ): DialogRef<TResult> {
    const size = options?.size ?? 'md';

    return this.dialog.open(component as any, {
      data: options?.data,
      disableClose: options?.disableClose ?? false,
      width: MODAL_SIZES[size],
      maxWidth: '95vw',
      panelClass: ['app-modal', `app-modal--${size}`],
    });
  }
}
