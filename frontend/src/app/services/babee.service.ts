import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Babee, BabeeList, UUID } from '../models/babee.model';

@Injectable({
  providedIn: 'root',
})
export class BabeeService {
  readonly #BACKEND_URL = environment.backendUrl;
  readonly #BABEE_URL = this.#BACKEND_URL + '/api/babee';

  private readonly httpClient = inject(HttpClient);

  getBabeeList(): Observable<BabeeList> {
    return this.httpClient.get<BabeeList>(this.#BABEE_URL);
  }

  getBabeeById(id: UUID): Observable<Babee> {
    return this.httpClient.get<Babee>(this.#BABEE_URL + '/' + id);
  }

  addBabee(babee: Babee): Observable<Babee> {
    return this.httpClient.post<Babee>(this.#BABEE_URL, babee);
  }

  updateBabee(babee: Babee): Observable<Babee> {
    return this.httpClient.put<Babee>(this.#BABEE_URL + '/' + babee.id, babee);
  }

  deleteBabee(id: UUID): Observable<void> {
    return this.httpClient.delete<void>(this.#BABEE_URL + '/' + id);
  }
}
