import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Activity, ActivityList, UUID } from '../models/babee.model';

@Injectable({
  providedIn: 'root',
})
export class ActivityService {
  readonly #BACKEND_URL = environment.backendUrl;
  readonly #BASE_URL = this.#BACKEND_URL + '/api/activity';

  private readonly httpClient = inject(HttpClient);

  getActivitiesByBabeeId(babeeId: UUID): Observable<ActivityList> {
    const params = new HttpParams().set('babeeId', babeeId);

    return this.httpClient.get<ActivityList>(this.#BASE_URL, { params });
  }

  getActivitiesByBabeeIdAndDate(
    babeeId: UUID,
    date: Date
  ): Observable<ActivityList> {
    const params = new HttpParams()
      .set('babeeId', babeeId)
      .set('eventDate', date.toLocaleDateString());

    return this.httpClient.get<ActivityList>(this.#BASE_URL, { params });
  }

  createActivity(activity: Activity): Observable<Activity> {
    return this.httpClient.post<Activity>(this.#BASE_URL, activity);
  }

  deleteActivity(activityId: UUID): Observable<void> {
    return this.httpClient.delete<void>(this.#BASE_URL + '/' + activityId);
  }
}
