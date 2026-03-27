/* Simple PWA Service Worker for Salah
 * - Caches core assets
 * - Receives SCHEDULE_PRAYER messages to set timers
 * - Shows notifications 20 minutes before and at prayer time
 */

const CACHE_NAME = 'salah-cache-v1';
const CORE_ASSETS = [
  '/',
  '/index.html',
  '/manifest.json',
  '/script.js'
];

self.addEventListener('install', (e) => {
  e.waitUntil(
    caches.open(CACHE_NAME).then((cache) => cache.addAll(CORE_ASSETS))
  );
  self.skipWaiting();
});

self.addEventListener('activate', (e) => {
  e.waitUntil(self.clients.claim());
});

self.addEventListener('fetch', (e) => {
  const req = e.request;
  if (req.method !== 'GET') return;
  e.respondWith(
    caches.match(req).then((cached) => cached || fetch(req))
  );
});

// In-memory schedule for this SW lifecycle
const scheduled = [];

function minutesToNextOccurrence(minutesOfDay) {
  const now = new Date();
  const target = new Date(now);
  target.setHours(Math.floor(minutesOfDay / 60), minutesOfDay % 60, 0, 0);
  if (target <= now) target.setDate(target.getDate() + 1);
  return target - now;
}

async function showPrayerNotification(title, body) {
  try {
    await self.registration.showNotification(title, {
      body,
      icon: 'other.png',
      badge: 'other.png',
      vibrate: [100, 50, 100],
      actions: [{ action: 'open', title: 'Open' }],
      tag: 'salah-prayer'
    });
  } catch (e) {
    // no-op
  }
}

function scheduleTimersForPrayer(name, minutesOfDay) {
  const msUntil = minutesToNextOccurrence(minutesOfDay);
  const before20 = Math.max(0, msUntil - 20 * 60 * 1000);

  setTimeout(() => {
    showPrayerNotification(`${name} in 20 minutes`, 'Time to prepare for salah.');
  }, before20);

  setTimeout(() => {
    showPrayerNotification(`${name} time`, 'The prayer time has started.');
  }, msUntil);
}

self.addEventListener('message', (event) => {
  const { type, payload } = event.data || {};
  if (type === 'SCHEDULE_PRAYER' && payload) {
    const { name, minutes } = payload;
    if (typeof minutes === 'number') {
      scheduleTimersForPrayer(name, minutes);
      scheduled.push({ name, minutes });
    }
  }
});

self.addEventListener('notificationclick', (event) => {
  event.notification.close();
  event.waitUntil(
    clients.matchAll({ type: 'window', includeUncontrolled: true }).then((clis) => {
      for (const c of clis) {
        if ('focus' in c) return c.focus();
      }
      if (clients.openWindow) return clients.openWindow('/');
    })
  );
});

// Wake up periodically if possible (fallback no-op on most browsers)
self.addEventListener('periodicsync', (event) => {
  if (event.tag === 'refresh-prayer-schedules') {
    event.waitUntil(
      self.registration.getNotifications({ tag: 'salah-prayer' }).then(() => Promise.resolve())
    );
  }
});


