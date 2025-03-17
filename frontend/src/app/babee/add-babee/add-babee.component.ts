import { Component, inject } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { BabeeService } from '../../services/babee.service';

@Component({
  selector: 'app-add-babee',
  imports: [ReactiveFormsModule],
  templateUrl: './add-babee.component.html',
  styleUrl: './add-babee.component.css',
})
export class AddBabeeComponent {
  readonly babeeService = inject(BabeeService);
  readonly router = inject(Router);

  readonly form = new FormGroup({
    firstName: new FormControl('', Validators.required),
    lastName: new FormControl('', Validators.required),
    birthDate: new FormControl('', Validators.required),
    photo: new FormControl(''),
  });

  onFileSelected(event: Event) {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        const base64String = reader.result as string;
        this.form.patchValue({ photo: base64String.split(',')[1] });
      };
      reader.readAsDataURL(file);
    }
  }

  get firstName(): FormControl {
    return this.form.get('firstName') as FormControl;
  }

  get lastName(): FormControl {
    return this.form.get('lastName') as FormControl;
  }

  get birthDate(): FormControl {
    return this.form.get('birthDate') as FormControl;
  }

  get photo(): FormControl {
    return this.form.get('photo') as FormControl;
  }

  onSubmit() {
    if (this.form.valid) {
      const babee = {
        firstName: this.firstName.value,
        lastName: this.lastName.value,
        birthDate: this.birthDate.value,
        photo: this.photo.value,
      };

      this.babeeService.addBabee(babee).subscribe((babee) => {
        this.router.navigate(['/bebe/' + babee.id]);
      });
    }
  }
}
