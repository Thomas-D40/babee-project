import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'age',
})
export class AgePipe implements PipeTransform {
  transform(birthDate: Date | string): string {
    if (!birthDate) return '';

    const birth = new Date(birthDate);
    const today = new Date();

    let months = today.getMonth() - birth.getMonth();
    let days = today.getDate() - birth.getDate();

    if (days < 0) {
      months--;
      const lastMonth = new Date(
        today.getFullYear(),
        today.getMonth(),
        0
      ).getDate();
      days += lastMonth;
    }

    if (months < 0) {
      months += 12;
    }

    return `${months} mois et ${days} jours`;
  }
}
