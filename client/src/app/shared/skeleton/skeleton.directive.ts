import {
  Directive,
  input,
  effect,
  inject,
  TemplateRef,
  ViewContainerRef,
  ComponentRef,
} from '@angular/core';
import { SkeletonComponent } from './skeleton.component';

@Directive({
  selector: '[skeleton]',
  standalone: true,
})
export class SkeletonDirective {
  skeleton = input<boolean>(false);

  width = input<string>('100%', { alias: 'skeletonWidth' });
  height = input<string>('1rem', { alias: 'skeletonHeight' });
  animate = input<'pulse' | 'shimmer' | 'none'>('shimmer', { alias: 'skeletonAnimate' });
  variant = input<'sm' | 'md' | 'lg' | 'pill'>('md', { alias: 'skeletonVariant' });
  rounded = input(true, { alias: 'skeletonRounded' });
  extraClass = input<string>('', { alias: 'skeletonExtraClass' });

  private templateRef = inject(TemplateRef<unknown>);
  private viewContainer = inject(ViewContainerRef);

  private skeletonRef: ComponentRef<SkeletonComponent> | null = null;

  constructor() {
    effect(() => {
      this.render();
    });
  }

  private render() {
    const loading = this.skeleton();

    this.viewContainer.clear();

    if (loading) {
      this.skeletonRef = this.viewContainer.createComponent(SkeletonComponent);

      this.skeletonRef.setInput('width', this.width());
      this.skeletonRef.setInput('height', this.height());
      this.skeletonRef.setInput('animate', this.animate());
      this.skeletonRef.setInput('variant', this.variant());
      this.skeletonRef.setInput('rounded', this.rounded());
      this.skeletonRef.setInput('extraClass', this.extraClass());
    } else {
      this.viewContainer.createEmbeddedView(this.templateRef);
      this.skeletonRef = null;
    }
  }
}
