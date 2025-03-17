import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { InformationList, Informations, UUID } from '../models/babee.model';

@Injectable({
  providedIn: 'root',
})
export class InformationService {
  readonly #BACKEND_URL = environment.backendUrl;
  readonly #BASE_URL = this.#BACKEND_URL + '/api/information';

  private readonly httpClient = inject(HttpClient);

  getInformationByBabeeId(babeeId: UUID): Observable<InformationList> {
    const params = new HttpParams().set('babeeId', babeeId);

    return this.httpClient.get<InformationList>(this.#BASE_URL, { params });
  }

  getInformationByBabeeIdAndDate(
    babeeId: UUID,
    date: Date
  ): Observable<InformationList> {
    const params = new HttpParams()
      .set('babeeId', babeeId)
      .set('eventDate', date.toLocaleDateString());

    return this.httpClient.get<InformationList>(this.#BASE_URL, { params });
  }

  createInformation(information: Informations): Observable<Informations> {
    return this.httpClient.post<Informations>(this.#BASE_URL, information);
  }

  deleteInformation(id: UUID): Observable<void> {
    return this.httpClient.delete<void>(this.#BASE_URL + '/' + id);
  }
}
