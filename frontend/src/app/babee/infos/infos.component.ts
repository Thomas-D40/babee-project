import { Component, inject, Input, signal, SimpleChanges } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { InformationList, UUID } from '../../models/babee.model';
import { InformationService } from '../../services/information.service';
import { stringToDateUTC } from '../../utils/app.utils';

@Component({
  selector: 'app-infos',
  imports: [ReactiveFormsModule],
  templateUrl: './infos.component.html',
  styleUrl: './infos.component.css',
})
export class InfosComponent {
  @Input() date!: string;
  @Input() babeeId!: UUID;

  private readonly informationsService = inject(InformationService);

  private readonly informationsSignal = signal<InformationList>([]);

  readonly isLoading = signal(true);

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['date'] || changes['babeeId']) {
      this.fetchInformations();
    }
  }

  private fetchInformations() {
    this.isLoading.set(true);

    this.informationsService
      .getInformationByBabeeIdAndDate(this.babeeId, stringToDateUTC(this.date))
      .subscribe({
        next: (informations) => {
          this.informationsSignal.set(informations);
          this.isLoading.set(false);
        },
        error: (err) => {
          console.error('Erreur lors de la récupération des activités', err);
        },
      });
  }

  informations(): InformationList {
    return this.informationsSignal();
  }

  readonly form = new FormGroup({
    comment: new FormControl('', [Validators.required]),
  });

  get comment(): FormControl {
    return this.form.get('comment') as FormControl;
  }

  onSubmit() {
    const babeeId = this.babeeId;
    const isFormValid = this.form.valid;
    const date = new Date();

    if (isFormValid && babeeId) {
      const information = {
        comment: this.comment.value,
        eventDate: date,
        babeeId: babeeId,
      };

      this.informationsService.createInformation(information).subscribe(() => {
        this.form.patchValue({ comment: '' });
        this.fetchInformations();
      });
    }
  }

  deleteInformation(id: UUID) {
    this.informationsService
      .deleteInformation(id)
      .subscribe(() => this.fetchInformations());
  }
}
