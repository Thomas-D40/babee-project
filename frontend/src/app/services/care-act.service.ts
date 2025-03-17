import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { CareAct, CareActList, UUID } from '../models/babee.model';

@Injectable({
  providedIn: 'root',
})
export class CareActService {
  readonly #BACKEND_URL = environment.backendUrl;
  readonly #BASE_URL = this.#BACKEND_URL + '/api/care-act';

  private readonly httpClient = inject(HttpClient);

  getCareActByBabeeId(babeeId: UUID): Observable<CareActList> {
    const params = new HttpParams().set('babeeId', babeeId);

    return this.httpClient.get<CareActList>(this.#BASE_URL, { params });
  }

  getCareActByBabeeIdAndDate(
    babeeId: UUID,
    date: Date
  ): Observable<CareActList> {
    const params = new HttpParams()
      .set('babeeId', babeeId)
      .set('eventDate', date.toLocaleDateString());

    return this.httpClient.get<CareActList>(this.#BASE_URL, { params });
  }

  createCareAct(careAct: CareAct): Observable<CareAct> {
    return this.httpClient.post<CareAct>(this.#BASE_URL, careAct);
  }

  deleteCareAct(id: UUID): Observable<void> {
    return this.httpClient.delete<void>(this.#BASE_URL + '/' + id);
  }
}
