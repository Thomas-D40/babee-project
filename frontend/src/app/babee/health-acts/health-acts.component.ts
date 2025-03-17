import { NgFor } from '@angular/common';
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
  HEALTH_ACT_TYPE_LIST,
  HealthActList,
  UUID,
} from '../../models/babee.model';
import { HeathActService } from '../../services/heath-act.service';
import { addSecondsToHour, stringToDateUTC } from '../../utils/app.utils';

@Component({
  selector: 'app-health-acts',
  imports: [NgFor, ReactiveFormsModule],
  templateUrl: './health-acts.component.html',
  styleUrl: './health-acts.component.css',
})
export class HealthActsComponent {
  @Input() date!: string;
  @Input() babeeId!: UUID;

  readonly healthActTypeList = HEALTH_ACT_TYPE_LIST;

  private readonly healthActService = inject(HeathActService);

  private readonly healthActListSignal = signal<HealthActList>([]);

  readonly isLoading = signal(true);

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['date'] || changes['babeeId']) {
      this.fetchHealthActList();
    }
  }

  private fetchHealthActList() {
    this.isLoading.set(true);

    this.healthActService
      .getHeathActByBabeeIdAndDate(this.babeeId, stringToDateUTC(this.date))
      .subscribe({
        next: (healthActList) => {
          this.healthActListSignal.set(healthActList);
          this.isLoading.set(false);
        },
        error: (err) => {
          console.error('Erreur lors de la récupération des activités', err);
        },
      });
  }

  healthActList(): HealthActList {
    return this.healthActListSignal();
  }

  getHealthActTypeLabel(id: number): string {
    return (
      HEALTH_ACT_TYPE_LIST.find((item) => item.id === id)?.label || 'Inconnu'
    );
  }

  readonly form = new FormGroup(
    {
      healthActType: new FormControl(1, [Validators.required]),
      temperature: new FormControl(''),
      medecine: new FormControl(''),
      dosage: new FormControl(''),
      actHour: new FormControl('08:00', [Validators.required]),
    },
    {
      validators: healthActDetailRequiredValidator(),
    }
  );

  get healthActType(): FormControl {
    return this.form.get('healthActType') as FormControl;
  }

  get temperature(): FormControl {
    return this.form.get('temperature') as FormControl;
  }

  get medecine(): FormControl {
    return this.form.get('medecine') as FormControl;
  }
  get dosage(): FormControl {
    return this.form.get('dosage') as FormControl;
  }
  get actHour(): FormControl {
    return this.form.get('actHour') as FormControl;
  }

  resetFormFields() {
    this.form.patchValue({
      temperature: '',
      medecine: '',
      dosage: '',
    });
  }

  onSubmit() {
    const isFormValid = this.form.valid;

    if (isFormValid) {
      const healthAct = {
        babeeId: this.babeeId,
        eventDate: new Date(),
        healthActType: this.healthActType.value,
        actHour: addSecondsToHour(this.actHour.value),
        temperature: this.temperature?.value,
        medecine: this.medecine?.value,
        dosage: this.dosage?.value,
      };

      this.healthActService.createHealthAct(healthAct).subscribe(() => {
        this.resetFormFields();
        this.fetchHealthActList();
      });
    }
  }

  deleteHealthAct(id: UUID) {
    this.healthActService.deleteHealthAct(id).subscribe(() => {
      this.fetchHealthActList();
    });
  }
}

function healthActDetailRequiredValidator(): ValidatorFn {
  return (form: AbstractControl): ValidationErrors | null => {
    const healthAct = form.get('healthActType')?.value;
    const temperature = form.get('temperature');
    const medecine = form.get('medecine');
    const dosage = form.get('dosage');

    temperature?.setErrors(null);
    medecine?.setErrors(null);
    dosage?.setErrors(null);

    if (healthAct == 1 && !temperature?.value) {
      temperature?.setErrors({ required: true });
    }

    if (healthAct == 2) {
      if (!medecine?.value) medecine?.setErrors({ required: true });
      if (!dosage?.value) dosage?.setErrors({ required: true });
    }

    return null;
  };
}
