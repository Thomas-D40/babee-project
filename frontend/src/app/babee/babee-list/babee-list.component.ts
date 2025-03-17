import { Component, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';
import { BabeeService } from '../../services/babee.service';
import { getImageSrc } from '../../utils/app.utils';

@Component({
  selector: 'app-babee-list',
  imports: [RouterLink],
  templateUrl: './babee-list.component.html',
  styleUrl: './babee-list.component.css',
})
export class BabeeListComponent {
  readonly #babeeService = inject(BabeeService);

  readonly babeeList = toSignal(this.#babeeService.getBabeeList(), {
    initialValue: [],
  });

  getImageSrc(imageUrl: string) {
    return getImageSrc(imageUrl);
  }
}
