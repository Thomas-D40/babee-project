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
  CARE_ACT_DETAIL_LIST,
  CARE_ACT_TYPE_LIST,
  CareAct,
  CareActList,
  UUID,
} from '../../models/babee.model';
import { CareActService } from '../../services/care-act.service';
import { stringToDateUTC } from '../../utils/app.utils';

@Component({
  selector: 'app-care-acts',
  imports: [NgFor, ReactiveFormsModule],
  templateUrl: './care-acts.component.html',
  styleUrl: './care-acts.component.css',
})
export class CareActsComponent {
  @Input({ required: true }) date!: string;
  @Input({ required: true }) babeeId!: UUID;

  private readonly careActService = inject(CareActService);

  private readonly careActSignals = signal<CareActList>([]);

  readonly isLoading = signal(true);

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['date'] || changes['babeeId']) {
      this.fetchCareActList();
    }
  }

  private fetchCareActList() {
    this.isLoading.set(true);

    this.careActService
      .getCareActByBabeeIdAndDate(this.babeeId, stringToDateUTC(this.date))
      .subscribe({
        next: (careActList) => {
          this.careActSignals.set(careActList);
          this.isLoading.set(false);
        },
        error: (err) => {
          console.error('Erreur lors de la récupération des activités', err);
        },
      });
  }

  careActList(): CareActList {
    return this.careActSignals();
  }

  getCareActTypeLabel(id: number): string {
    return (
      CARE_ACT_TYPE_LIST.find((item) => item.id === id)?.label || 'Inconnu'
    );
  }

  getCareActDetailLabel(id: number | undefined): string {
    if (id) {
      return (
        CARE_ACT_DETAIL_LIST.find((item) => item.id === id)?.label || 'Inconnu'
      );
    }

    return '';
  }

  readonly careActOptionList = CARE_ACT_TYPE_LIST;
  readonly careDetailOptionList = CARE_ACT_DETAIL_LIST;

  readonly form = new FormGroup(
    {
      careActType: new FormControl('', [Validators.required]),
      careActDetail: new FormControl(''),
      commentaire: new FormControl(''),
    },
    {
      validators: careDetailRequiredValidator(),
    }
  );

  get careActType(): FormControl {
    return this.form.get('careActType') as FormControl;
  }

  get careActDetail(): FormControl {
    return this.form.get('careActDetail') as FormControl;
  }

  onCareActCheckboxChange(optionId: number): void {
    if ([3, 4].includes(optionId)) {
      this.careActDetail.setValue(null);
    }
    this.careActType.setValue(
      this.careActType.value === optionId ? null : optionId
    );
  }

  onCareDetailCheckboxChange(optionId: number): void {
    this.careActDetail.setValue(
      this.careActDetail.value === optionId ? null : optionId
    );
  }

  onSubmit() {
    const careActType = this.form.get('careActType') as FormControl;
    const careActDetail = this.form.get('careActDetail') as FormControl;
    const commentaire = this.form.get('commentaire') as FormControl;
    const isFormValid = this.form.valid;
    const babeeId = this.babeeId;
    const date = new Date();

    if (isFormValid && babeeId) {
      const careAct: CareAct = {
        eventDate: date,
        babeeId: babeeId,
        careActType: careActType.value,
        careActDetail: careActDetail.value,
        comment: commentaire.value,
      };

      this.careActService.createCareAct(careAct).subscribe(() => {
        this.form.patchValue({
          careActType: null,
          careActDetail: null,
          commentaire: '',
        });
        this.fetchCareActList();
      });
    }
  }

  deleteCareAct(id: UUID) {
    this.careActService.deleteCareAct(id).subscribe(() => {
      this.fetchCareActList();
    });
  }
}

function careDetailRequiredValidator(): ValidatorFn {
  return (form: AbstractControl): ValidationErrors | null => {
    const careActType = form.get('careActType')?.value;
    const careActDetail = form.get('careActDetail')?.value;

    if ([1, 2].includes(careActType) && !careActDetail) {
      return { careActDetailRequired: true };
    }
    return null;
  };
}
