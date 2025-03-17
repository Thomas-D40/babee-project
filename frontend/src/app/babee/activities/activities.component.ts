import {
  Component,
  inject,
  Input,
  OnChanges,
  signal,
  SimpleChanges,
} from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivityList, UUID } from '../../models/babee.model';
import { ActivityService } from '../../services/activity.service';
import { stringToDateUTC } from '../../utils/app.utils';

@Component({
  selector: 'app-activities',
  imports: [ReactiveFormsModule],
  templateUrl: './activities.component.html',
  styleUrl: './activities.component.css',
})
export class ActivitiesComponent implements OnChanges {
  @Input({ required: true }) date!: string;
  @Input({ required: true }) babeeId!: UUID;

  private readonly activityService = inject(ActivityService);

  private readonly activitesSignal = signal<ActivityList>([]);

  readonly isLoading = signal(true);

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['date'] || changes['babeeId']) {
      this.fetchActivities();
    }
  }

  private fetchActivities() {
    this.isLoading.set(true);

    this.activityService
      .getActivitiesByBabeeIdAndDate(this.babeeId, stringToDateUTC(this.date))
      .subscribe({
        next: (activities) => {
          this.activitesSignal.set(activities);
          this.isLoading.set(false);
        },
        error: (err) => {
          console.error('Erreur lors de la récupération des activités', err);
        },
      });
  }

  activites(): ActivityList {
    return this.activitesSignal();
  }

  readonly form = new FormGroup({
    activityName: new FormControl('', [Validators.required]),
  });

  onSubmit() {
    const babeeId = this.babeeId;
    const isFormValid = this.form.valid;
    const date = new Date();
    const activityName = this.form.get('activityName') as FormControl;

    if (isFormValid && babeeId) {
      const activity = {
        name: activityName.value,
        eventDate: date,
        babeeId: babeeId,
      };

      this.activityService.createActivity(activity).subscribe(() => {
        this.form.patchValue({ activityName: '' });
        this.fetchActivities();
      });
    }
  }

  deleteActivity(activityId: UUID) {
    this.activityService
      .deleteActivity(activityId)
      .subscribe(() => this.fetchActivities());
  }
}
