import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Feeding, FeedingList, UUID } from '../models/babee.model';

@Injectable({
  providedIn: 'root',
})
export class FeedingService {
  readonly #BACKEND_URL = environment.backendUrl;
  readonly #BASE_URL = this.#BACKEND_URL + '/api/feeding';

  private readonly httpClient = inject(HttpClient);

  getFeedingByBabeeId(babeeId: UUID): Observable<FeedingList> {
    const params = new HttpParams().set('babeeId', babeeId);

    return this.httpClient.get<FeedingList>(this.#BASE_URL, { params });
  }

  getFeedingByBabeeIdAndDate(
    babeeId: UUID,
    date: Date
  ): Observable<FeedingList> {
    const params = new HttpParams()
      .set('babeeId', babeeId)
      .set('eventDate', date.toLocaleDateString());

    return this.httpClient.get<FeedingList>(this.#BASE_URL, { params });
  }

  createFeeding(feeding: Feeding): Observable<Feeding> {
    return this.httpClient.post<Feeding>(this.#BASE_URL, feeding);
  }

  deleteFeeding(id: UUID): Observable<void> {
    return this.httpClient.delete<void>(this.#BASE_URL + '/' + id);
  }
}
