var CACHE_NAME = 'song-catalog-cache-v1';

self.addEventListener('install', function(event) {
  var urlsToCache = [
    'images/app.png',
    'images/meals.png',
    'images/splash_1170x2532.png'
  ];
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then(function(cache) {
        console.log('Opened cache');
        return cache.addAll(urlsToCache);
      })
  );
});

self.addEventListener('fetch', function(event) {
  event.respondWith(
    caches.match(event.request)
      .then(function(response) {
        if (response) {
          return response;
        }
        return fetch(event.request);
      }
    )
  );
});