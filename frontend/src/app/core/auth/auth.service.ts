import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { map, Observable } from 'rxjs';
import { User } from '../../models/babee.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private user = signal<User>({} as User);
  readonly #BACKEND_URL = 'http://localhost:3000';
  readonly #BASE_URL = this.#BACKEND_URL + '/users';

  constructor(private router: Router, private httpClient: HttpClient) {}

  login(username: string, password: string): Observable<boolean> {
    const params = new HttpParams()
      .set('username', username)
      .set('password', password);

    return this.httpClient.get<any[]>(this.#BASE_URL, { params }).pipe(
      map((users) => {
        if (users.length > 0) {
          const user = users[0];
          this.user.set(user);
          localStorage.setItem('user', JSON.stringify(user));
          return true;
        }
        return false;
      })
    );
  }

  setUser(user: { role: string; bebeId?: number }) {
    this.user.set(user);
  }

  getUser(): User {
    return this.user();
  }

  isCrecheStaff(): boolean {
    return this.user()?.role === 'creche';
  }

  isParent(): boolean {
    return this.user()?.role === 'parent';
  }

  getBebeId(): number | undefined {
    return this.user()?.bebeId;
  }
}
