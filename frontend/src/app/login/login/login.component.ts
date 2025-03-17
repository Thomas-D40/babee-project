import { Component } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  constructor(private authService: AuthService, private router: Router) {}

  readonly form = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  });

  get username(): FormControl {
    return this.form.get('username') as FormControl;
  }

  get password(): FormControl {
    return this.form.get('password') as FormControl;
  }

  login() {
    if (this.form.valid) {
      this.authService
        .login(this.username.value, this.password.value)
        .subscribe((success) => {
          if (success) {
            const user = this.authService.getUser();
            if (user.role === 'creche') {
              this.router.navigate(['/bebe']);
            } else {
              this.router.navigate([`/bebe/${user.bebeId}`]);
            }
          } else {
            this.form.setErrors({ invalidLogin: true });
          }
        });
    }
  }
}
