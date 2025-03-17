import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { HealthAct, HealthActList, UUID } from '../models/babee.model';

@Injectable({
  providedIn: 'root',
})
export class HeathActService {
  readonly #BACKEND_URL = environment.backendUrl;
  readonly #BASE_URL = this.#BACKEND_URL + '/api/health-act';

  private readonly httpClient = inject(HttpClient);

  getHeathActByBabeeId(babeeId: UUID): Observable<HealthActList> {
    const params = new HttpParams().set('babeeId', babeeId);

    return this.httpClient.get<HealthActList>(this.#BASE_URL, { params });
  }

  getHeathActByBabeeIdAndDate(
    babeeId: UUID,
    date: Date
  ): Observable<HealthActList> {
    const params = new HttpParams()
      .set('babeeId', babeeId)
      .set('eventDate', date.toLocaleDateString());

    return this.httpClient.get<HealthActList>(this.#BASE_URL, { params });
  }

  createHealthAct(healthAct: HealthAct): Observable<HealthAct> {
    return this.httpClient.post<HealthAct>(this.#BASE_URL, healthAct);
  }

  deleteHealthAct(id: UUID): Observable<void> {
    return this.httpClient.delete<void>(this.#BASE_URL + '/' + id);
  }
}
