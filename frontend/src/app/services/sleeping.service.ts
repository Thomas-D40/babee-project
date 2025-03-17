import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Sleeping, SleepingList, UUID } from '../models/babee.model';

@Injectable({
  providedIn: 'root',
})
export class SleepingService {
  readonly #BACKEND_URL = environment.backendUrl;
  readonly #BASE_URL = this.#BACKEND_URL + '/api/sleeping';

  private readonly httpClient = inject(HttpClient);

  getSleepingByBabeeId(babeeId: UUID): Observable<SleepingList> {
    const params = new HttpParams().set('babeeId', babeeId);

    return this.httpClient.get<SleepingList>(this.#BASE_URL, { params });
  }

  getSleepingByBabeeIdAndDate(
    babeeId: UUID,
    date: Date
  ): Observable<SleepingList> {
    const params = new HttpParams()
      .set('babeeId', babeeId)
      .set('eventDate', date.toLocaleDateString());

    return this.httpClient.get<SleepingList>(this.#BASE_URL, { params });
  }

  createSleeping(sleep: Sleeping): Observable<Sleeping> {
    return this.httpClient.post<Sleeping>(this.#BASE_URL, sleep);
  }

  deleteSleeping(id: UUID): Observable<void> {
    return this.httpClient.delete<void>(this.#BASE_URL + '/' + id);
  }
}
