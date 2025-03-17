import { Component, computed, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { catchError, map, of } from 'rxjs';
import { UUID } from '../../models/babee.model';
import { AgePipe } from '../../pipes/age.pipe';
import { BabeeService } from '../../services/babee.service';
import { getImageSrc } from '../../utils/app.utils';
import { ActivitiesComponent } from '../activities/activities.component';
import { CareActsComponent } from '../care-acts/care-acts.component';
import { FeedingComponent } from '../feeding/feeding.component';
import { HealthActsComponent } from '../health-acts/health-acts.component';
import { InfosComponent } from '../infos/infos.component';
import { SleepingComponent } from '../sleeping/sleeping.component';

@Component({
  selector: 'app-babee-profile',
  imports: [
    ActivitiesComponent,
    FeedingComponent,
    HealthActsComponent,
    CareActsComponent,
    SleepingComponent,
    InfosComponent,
    ReactiveFormsModule,
    AgePipe,
  ],
  templateUrl: './babee-profile.component.html',
  styleUrl: './babee-profile.component.css',
})
export class BabeeProfileComponent {
  readonly #babeeService = inject(BabeeService);
  readonly #route: ActivatedRoute = inject(ActivatedRoute);
  readonly babeeId: UUID = this.#route.snapshot.paramMap.get('id') as UUID;

  readonly selectedDate = new FormControl(
    new Date().toISOString().slice(0, 10)
  );

  readonly #babeeResponse = toSignal(
    this.#babeeService.getBabeeById(this.babeeId).pipe(
      map((babee) => ({ value: babee, error: undefined })),
      catchError((error) => of({ value: undefined, error: error }))
    )
  );

  readonly loading = computed(() => this.#babeeResponse() === undefined);

  readonly error = computed(() => !!this.#babeeResponse()?.error);

  readonly babee = computed(() => this.#babeeResponse()?.value);

  getImageSrc(imageUrl: string) {
    return getImageSrc(imageUrl);
  }
}
