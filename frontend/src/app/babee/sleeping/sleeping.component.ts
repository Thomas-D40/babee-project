import { Component, inject, Input, signal, SimpleChanges } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import {
  HORAIRES_FERMETURE,
  HORAIRES_OUVERTURE,
} from '../../constants/app.constants';
import { SleepingList, UUID } from '../../models/babee.model';
import { TimeDiffPipe } from '../../pipes/time-diff.pipe';
import { SleepingService } from '../../services/sleeping.service';
import { addSecondsToHour, stringToDateUTC } from '../../utils/app.utils';

@Component({
  selector: 'app-sleeping',
  imports: [TimeDiffPipe, ReactiveFormsModule],
  templateUrl: './sleeping.component.html',
  styleUrl: './sleeping.component.css',
})
export class SleepingComponent {
  @Input() date!: string;
  @Input() babeeId!: UUID;

  private readonly sleepingService = inject(SleepingService);

  private readonly sleepingListSignal = signal<SleepingList>([]);

  readonly isLoading = signal(true);

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['date'] || changes['babeeId']) {
      this.fetchSleeping();
    }
  }

  private fetchSleeping() {
    this.isLoading.set(true);

    this.sleepingService
      .getSleepingByBabeeIdAndDate(this.babeeId, stringToDateUTC(this.date))
      .subscribe({
        next: (sleepinList) => {
          this.sleepingListSignal.set(sleepinList);
          this.isLoading.set(false);
        },
        error: (err) => {
          console.error('Erreur lors de la récupération des activités', err);
        },
      });
  }

  sleepingList(): SleepingList {
    return this.sleepingListSignal();
  }

  readonly form = new FormGroup(
    {
      beginHour: new FormControl('08:00', [Validators.required]),
      endHour: new FormControl('08:05', [Validators.required]),
    },
    { validators: timeRangeValidator() }
  );

  get beginHour(): FormControl {
    return this.form.get('beginHour') as FormControl;
  }

  get endHour(): FormControl {
    return this.form.get('endHour') as FormControl;
  }

  onSubmit() {
    const isFormValid = this.form.valid;

    if (isFormValid) {
      const beginHour = this.beginHour.value;
      const endHour = this.endHour.value;
      const babeeId = this.babeeId;
      const date = new Date();

      const sleeping = {
        beginHour: addSecondsToHour(beginHour),
        endHour: addSecondsToHour(endHour),
        babeeId: babeeId,
        eventDate: date,
      };

      this.sleepingService.createSleeping(sleeping).subscribe(() => {
        this.form.patchValue({
          beginHour: '08:00',
          endHour: '08:05',
        });
        this.fetchSleeping();
      });
    }
  }

  openTimePicker() {
    const input = document.getElementById('timeInput') as HTMLInputElement;
    input?.focus();
  }

  deleteSleeping(id: UUID) {
    this.sleepingService.deleteSleeping(id).subscribe(() => {
      this.fetchSleeping();
    });
  }
}

export function timeRangeValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const debut = control.get('debut')?.value;
    const fin = control.get('fin')?.value;

    if (!debut || !fin) {
      return null;
    }

    const start = convertToMinutes(debut);
    const end = convertToMinutes(fin);

    const minStart = convertToMinutes(HORAIRES_OUVERTURE);
    const maxEnd = convertToMinutes(HORAIRES_FERMETURE);

    const errors: ValidationErrors = {};

    if (start < minStart) {
      errors['startTooEarly'] = "L'heure de début doit être après 08:00";
    }

    if (end > maxEnd) {
      errors['endTooLate'] = "L'heure de fin doit être avant 18:45";
    }

    if (start >= end) {
      errors['invalidRange'] =
        "L'heure de début doit être avant l'heure de fin";
    }

    return Object.keys(errors).length ? errors : null;
  };
}

function convertToMinutes(time: string): number {
  const [hours, minutes] = time.split(':').map(Number);
  return hours * 60 + minutes;
}
