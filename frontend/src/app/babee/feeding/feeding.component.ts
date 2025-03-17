import { NgFor } from '@angular/common';
import {
  Component,
  computed,
  inject,
  Input,
  signal,
  SimpleChanges,
} from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { FeedingList, UUID } from '../../models/babee.model';
import { FeedingService } from '../../services/feeding.service';
import { stringToDateUTC } from '../../utils/app.utils';

@Component({
  selector: 'app-feeding',
  imports: [NgFor, FormsModule, ReactiveFormsModule],
  templateUrl: './feeding.component.html',
  styleUrl: './feeding.component.css',
})
export class FeedingComponent {
  @Input() date!: string;
  @Input() babeeId!: UUID;

  mealOptions = ['A gouté', 'En partie', 'Entièrement'];

  private readonly feedingService = inject(FeedingService);

  private readonly feedingListSignal = signal<FeedingList>([]);

  feedingList(): FeedingList {
    return this.feedingListSignal();
  }

  readonly isLoading = signal(true);

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['date'] || changes['babeeId']) {
      this.fetchFeedingList();
    }
  }

  private fetchFeedingList() {
    this.isLoading.set(true);

    this.feedingService
      .getFeedingByBabeeIdAndDate(this.babeeId, stringToDateUTC(this.date))
      .subscribe({
        next: (feedingList) => {
          this.feedingListSignal.set(feedingList);
          this.isLoading.set(false);
        },
        error: (err) => {
          console.error('Erreur lors de la récupération des activités', err);
        },
      });
  }

  repas_V2: { [key: string]: string[] } = {
    biberon: [], // Voir pour se débarasser de biberons
    petit: [
      'Sauté de porc',
      'Colin',
      'Purée de céléri',
      'Purée pomme groseille',
    ],
    moyen: [
      'Sauté de porc',
      'Colin',
      'Purée de céléri',
      'Fraidou',
      'Purée pomme groseille',
    ],
    grand: [
      'Sauté de porc',
      'Colin',
      'Semoule',
      'Fraidou',
      'Purée pomme groseille',
    ],
    gouter_petit: ['yaourt nature', 'compote pommes badiane'],
    gouter_moyen: ['yaourt nature', 'compote pommes badiane', 'pain'],
    gouter_grand: ['yaourt nature', 'compote pommes badiane', 'pain'],
  };

  selectedKey = signal<'biberon' | 'petit' | 'moyen' | 'grand'>('petit');

  // MEAL
  mealFormGroup = computed<FormGroup>(() => this.createMealForm());

  constructor(private fb: FormBuilder) {}

  createMealForm(): FormGroup {
    const group = {} as Record<string, FormControl>;

    const key = this.selectedKey();
    if (key && this.repas_V2[key]) {
      this.repas_V2[key].forEach((plat) => {
        this.mealOptions.forEach((option) => {
          const controlName = this.getMealControlName(plat, option);
          group[controlName] = new FormControl(false, { nonNullable: true });
        });
      });
    }

    console.log('FormGroup updated:', group);
    return this.fb.group(group);
  }

  getMealControlName(ligne: string, option: string): string {
    return `${ligne}-${option}`.replace(/\s+/g, '_');
  }

  getMealControl(ligne: string, option: string): FormControl {
    const controlName = this.getMealControlName(ligne, option);
    return this.mealFormGroup().get(controlName) as FormControl;
  }

  toggleCheckbox(ligne: string, option: string): void {
    const control = this.getMealControl(ligne, option);
    control.setValue(!control.value);
  }

  objectKeys(object: Object) {
    return Object.keys(object);
  }

  onMealFormSubmit() {
    const informations: Map<string, string> = new Map([]);
    const mealInformations: string[] = [];
    const formValues = this.mealFormGroup().value;
    const isFormValid = this.mealFormGroup().valid;

    if (isFormValid) {
      Object.keys(formValues).forEach((key: string) => {
        if (formValues[key]) {
          let [meal, quantity] = key.split('-');
          meal = meal.replace(/_/g, ' ');
          quantity = quantity.replace(/_/g, ' ');
          mealInformations.push(meal + ' - ' + quantity);
        }
      });

      const feeding = {
        babeeId: this.babeeId,
        eventDate: new Date(),
        feedingInformations: mealInformations,
      };

      this.feedingService.createFeeding(feeding).subscribe(() => {
        this.fetchFeedingList();
      });
    }
  }

  // Bottle
  readonly bottleForm = new FormGroup({
    quantity: new FormControl('', [Validators.required]),
  });

  get quantity(): FormControl {
    return this.bottleForm.get('quantity') as FormControl;
  }

  onBottleFormSubmit() {
    const isFormValid = this.bottleForm.valid;

    if (isFormValid) {
      const feeding = {
        babeeId: this.babeeId,
        eventDate: new Date(),
        feedingInformations: ['Biberon - ' + this.quantity.value],
      };

      this.feedingService.createFeeding(feeding).subscribe(() => {
        this.fetchFeedingList();
      });
    }
  }

  deleteFeeding(id: UUID) {
    this.feedingService.deleteFeeding(id).subscribe(() => {
      this.fetchFeedingList();
    });
  }
}
