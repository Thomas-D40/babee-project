import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'timeDiff' })
export class TimeDiffPipe implements PipeTransform {
  transform(start: string, end: string): string {
    if (!start || !end) return '00m';

    // Convertir hh:mm en minutes
    const [h1, m1] = start.split(':').map(Number);
    const [h2, m2] = end.split(':').map(Number);

    const totalMinutes1 = h1 * 60 + m1;
    const totalMinutes2 = h2 * 60 + m2;

    // Calcul de la différence
    const diff = totalMinutes2 - totalMinutes1;
    if (diff <= 0) return '00m';

    // Conversion en hh:mm
    const hours = Math.floor(diff / 60);
    const minutes = diff % 60;

    return hours > 0
      ? `${hours}h${minutes.toString().padStart(2, '0')}m`
      : `${minutes}m`;
  }
}
