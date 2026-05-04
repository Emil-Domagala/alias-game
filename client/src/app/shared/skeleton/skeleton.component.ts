import {Component, computed, input} from '@angular/core';

@Component({
  selector: 'app-skeleton',
  host: {
    class: 'skeleton',
    '[class]': 'classes()',
    '[style.width]': 'width()',
    '[style.height]': 'height()',
  },
  template: ``,
  styleUrl: './skeleton.component.scss',
})
export class SkeletonComponent {
  width = input<string>('100%');
  height = input<string>('1rem');

  animate = input<'pulse' | 'shimmer' | 'none'>('shimmer');

  variant = input<'sm' | 'md' | 'lg' | 'pill'>('md');

  rounded = input(true);

  extraClass = input<string>('');

  classes = computed(() => [
    this.variant(),
    this.animate(),
    this.rounded() ? 'rounded' : '',
    this.extraClass(),
  ]);
}
