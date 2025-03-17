export function getStartAndEndOfDay(date: Date): {
  startOfDay: string;
  endOfDay: string;
} {
  const startOfDay = new Date(
    date.getFullYear(),
    date.getMonth(),
    date.getDate(),
    0,
    0,
    0,
    0
  );

  const endOfDay = new Date(
    date.getFullYear(),
    date.getMonth(),
    date.getDate(),
    23,
    59,
    59,
    999
  );

  return {
    startOfDay: startOfDay.toISOString(),
    endOfDay: endOfDay.toISOString(),
  };
}

export function stringToDateUTC(dateString: string) {
  if (!dateString) {
    return new Date();
  }
  const [year, month, day] = dateString.split('-').map(Number);

  return new Date(Date.UTC(year, month - 1, day));
}

export function addSecondsToHour(hour: string) {
  return hour + ':00';
}

export function getImageSrc(base64: string | null): string {
  if (base64) {
    return `data:image/jpeg;base64,${base64}`;
  }
  return 'images/avatar-bebe.png';
}
