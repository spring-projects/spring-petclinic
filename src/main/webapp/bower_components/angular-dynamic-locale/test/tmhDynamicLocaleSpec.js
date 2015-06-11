(function () {
  'use strict';

  // Minimal implementation to mock what was removed from Jasmine 1.x
  function createAsync(doneFn) {
    function Job() {
      this.next = [];
    }
    Job.prototype.done = function () {
      return this.runs(doneFn);
    };
    Job.prototype.runs = function (fn) {
      var newJob = new Job();
      this.next.push(function () {
        fn();
        newJob.start();
      });
      return newJob;
    };
    Job.prototype.waitsFor = function (fn, error, timeout) {
      var newJob = new Job();
      timeout = timeout || 5000;
      this.next.push(function () {
        var counter = 0,
          intervalId = window.setInterval(function () {
            if (fn()) {
              window.clearInterval(intervalId);
              newJob.start();
            }
            counter += 5;
            if (counter > timeout) {
              window.clearInterval(intervalId);
              throw new Error(error);
            }
          }, 5);
      });
      return newJob;
    };
    Job.prototype.start = function () {
      var i;
      for (i = 0; i < this.next.length; i += 1) {
        this.next[i]();
      }
    };
    return new Job();
  }

  describe('dynamicLocale', function() {
    beforeEach(module('tmh.dynamicLocale'));
    beforeEach(module(function(tmhDynamicLocaleProvider) {
      tmhDynamicLocaleProvider.localeLocationPattern('/base/node_modules/angular-i18n/angular-locale_{{locale}}.js');
    }));

    afterEach(function (done) {
      inject(function($locale, $timeout, tmhDynamicLocale) {
        var job = createAsync(done);
        job
          .runs(function() {
            tmhDynamicLocale.set('en-us');
          })
          .waitsFor(function() {
            $timeout.flush(50);
            return $locale.id === 'en-us';
          }, 'locale not reverted', 2000)
          .done();
        job.start();
      });
    });

    it('should (eventually) be able to change the locale', function(done) {
      inject(function($locale, $timeout, tmhDynamicLocale) {
        var job = createAsync(done);
        job
          .runs(function() {
            tmhDynamicLocale.set('es');
          })
          .waitsFor(function() {
            $timeout.flush(50);
            return $locale.id === 'es';
          }, 'locale not updated', 2000)
          .runs(function() {
            expect($locale.id).toBe('es');
            expect($locale.DATETIME_FORMATS.DAY["0"]).toBe("domingo");
          })
          .done();
        job.start();
      });
    });

    it('should trigger an event when there it changes the locale', function(done) {
      inject(function($timeout, $locale, tmhDynamicLocale, $rootScope) {
        var callback = jasmine.createSpy();
        var job = createAsync(done);
        job
          .runs(function() {
            $rootScope.$apply();
            $rootScope.$on('$localeChangeSuccess', callback);
            tmhDynamicLocale.set('es');
            expect(callback.calls.count()).toBe(0);
          })
          .waitsFor(function() {
            $timeout.flush(50);
            return $locale.id === 'es';
          }, 'locale not updated', 2000)
          .runs(function() {
            expect(callback.calls.count()).toBe(1);
            expect(callback.calls.argsFor(0)[1]).toEqual('es');
            expect(callback.calls.argsFor(0)[2]).toEqual($locale);
          })
          .done();
        job.start();
      });
    });

    it('should trigger a failure even when the locale change fail', function(done) {
      inject(function($timeout, $locale, tmhDynamicLocale, $rootScope) {
        var job = createAsync(done);
        var callback = jasmine.createSpy();

        job
          .runs(function() {
             $rootScope.$apply();
             $rootScope.$on('$localeChangeError', callback);
             tmhDynamicLocale.set('invalidLocale');
             expect(callback.calls.count()).toBe(0);
          })
          .waitsFor(function() {
            $timeout.flush(50);
            return callback.calls.count() !== 0;
          }, 'error not generated', 2000)
          .runs(function() {
            expect(callback.calls.count()).toBe(1);
            expect(callback.calls.argsFor(0)[1]).toEqual('invalidLocale');
          })
          .done();
        job.start();
      });
    });

    it('should return a promise that has the new locale', function(done) {
      inject(function($timeout, $locale, tmhDynamicLocale, $rootScope) {
        var job = createAsync(done);
        var callback = jasmine.createSpy();

        job
          .runs(function() {
            tmhDynamicLocale.set('es').then(callback);
            expect(callback.calls.count()).toBe(0);
          })
          .waitsFor(function() {
            $timeout.flush(50);
            return callback.calls.count() !== 0;
          }, 'locale not updated', 2000)
          .runs(function() {
            expect(callback.calls.argsFor(0)[0].id).toEqual('es');
            expect(callback.calls.argsFor(0)[0]).toEqual($locale);
            tmhDynamicLocale.set('it');
          })
          .waitsFor(function() {
            $timeout.flush(50);
            return $locale.id === 'it';
          }, 'locale not updated', 2000)
          .runs(function() {
            tmhDynamicLocale.set('es').then(callback);
            expect(callback.calls.count()).toBe(1);
            $rootScope.$apply();
            expect(callback.calls.count()).toBe(2);
            expect(callback.calls.argsFor(1)[0].id).toBe('es');
            expect(callback.calls.argsFor(1)[0]).toBe($locale);
          })
          .done();
        job.start();
      });
    });

    it('should reject the returned promise if it fails to load the locale', function(done) {
      inject(function($timeout, $locale, tmhDynamicLocale, $rootScope) {
        var callback = jasmine.createSpy();
        var errorCallback = jasmine.createSpy();
        var job = createAsync(done);

        job
          .runs(function() {
            tmhDynamicLocale.set('invalidLocale').then(callback, errorCallback);
          })
          .waitsFor(function() {
            $timeout.flush(50);
            return errorCallback.calls.count();
          }, 'promise not rejected', 2000)
          .runs(function() {
            expect(callback.calls.count()).toBe(0);
            expect(errorCallback.calls.count()).toBe(1);
            expect(errorCallback.calls.argsFor(0)[0]).toBe('invalidLocale');
            expect($locale.id).toBe('en-us');
          })
          .done();
        job.start();
      });
    });

    it('should be possible to retrieve the locale to be', function(done) {
      inject(function($timeout, $locale, tmhDynamicLocale, $rootScope, $compile) {
        var job = createAsync(done);

        job
          .runs(function() {
            tmhDynamicLocale.set('es');
            expect(tmhDynamicLocale.get()).toBe('es');
          })
          .waitsFor(function() {
            $timeout.flush(50);
            return $locale.id === 'es';
          }, 'locale not updated', 2000)
          .runs(function() {
            expect(tmhDynamicLocale.get()).toBe('es');
          })
          .done();
        job.start();
      });
    });

    it('should revert the configured locale when the new locale does not exist', function(done) {
      inject(function($timeout, $locale, tmhDynamicLocale, $rootScope) {
        var job = createAsync(done);
        var errorCallback = jasmine.createSpy();

        job
          .runs(function() {
            tmhDynamicLocale.set('es');
          })
          .waitsFor(function() {
            $timeout.flush(50);
            return $locale.id === 'es';
          }, 'locale not updated', 2000)
          .runs(function() {
            tmhDynamicLocale.set('invalidLocale').then(undefined, errorCallback);
            expect(tmhDynamicLocale.get()).toBe('invalidLocale');
          })
          .waitsFor(function() {
            $timeout.flush(50);
            return errorCallback.calls.count();
          }, 'promise not rejected', 2000)
          .runs(function() {
            expect(tmhDynamicLocale.get()).toBe('es');
          })
          .done();
        job.start();
      });
    });

    it('should change the already formatted numbers in the page', function(done) {
      inject(function($timeout, $locale, tmhDynamicLocale, $rootScope, $compile) {
        var job = createAsync(done);
        var element = null;

        job
          .runs(function() {
            element = $compile('<span>{{val | number}}</span>')($rootScope);

            $rootScope.val = 1234.5678;
            $rootScope.$apply();
            expect(element.text()).toBe('1,234.568');

            tmhDynamicLocale.set('es');
          })
          .waitsFor(function() {
            $timeout.flush(50);
            return $locale.id === 'es';
          }, 'locale not updated', 2000)
          .runs(function() {
            expect(element.text()).toBe('1.234,568');
          })
          .done();
        job.start();
      });
    });

    it('should keep already loaded locales at tmhDynamicLocaleCache', function(done) {
      inject(function($timeout, $locale, tmhDynamicLocale, tmhDynamicLocaleCache, $rootScope) {
        var job = createAsync(done);
        var callback = jasmine.createSpy();
        var esLocale = null;

        job
          .runs(function() {
            expect(tmhDynamicLocaleCache.info().size).toBe(0);
            tmhDynamicLocale.set('es');
            expect(tmhDynamicLocaleCache.info().size).toBe(0);
          })
          .waitsFor(function() {
            $timeout.flush(50);
            return $locale.id === 'es';
          }, 'locale not updated', 2000)
          .runs(function() {
            expect(tmhDynamicLocaleCache.info().size).toBe(1);
            expect(tmhDynamicLocaleCache.get('es')).toEqual($locale);
            esLocale = angular.copy($locale);
            tmhDynamicLocale.set('it');
          })
          .waitsFor(function() {
            $timeout.flush(50);
            return $locale.id === 'it';
          }, 'locale not updated', 2000)
          .runs(function() {
            expect(tmhDynamicLocaleCache.info().size).toBe(2);
            expect(tmhDynamicLocaleCache.get('es')).toEqual(esLocale);
            expect(tmhDynamicLocaleCache.get('it')).toEqual($locale);
          })
          .done();
        job.start();
      });
    });

    it('should use the cache when possible', function(done) {
      inject(function($timeout, $locale, tmhDynamicLocale, tmhDynamicLocaleCache, $rootScope) {
        var job = createAsync(done);
        var callback = jasmine.createSpy();

        job
          .runs(function() {
            tmhDynamicLocale.set('es');
          })
          .waitsFor(function() {
            $timeout.flush(50);
            return $locale.id === 'es';
          }, 'locale not updated', 2000)
          .runs(function() {
            tmhDynamicLocale.set('it');
          })
          .waitsFor(function() {
            $timeout.flush(50);
            return $locale.id === 'it';
          }, 'locale not updated', 2000)
          .runs(function() {
            tmhDynamicLocaleCache.get('es').DATETIME_FORMATS.DAY["0"] = "Domingo";
            $rootScope.$on('$localeChangeSuccess', callback);
            tmhDynamicLocale.set('es');
            // Changing the locale should be done async even when this is done from the cache
            expect(callback.calls.count()).toBe(0);
            expect($locale.id).toBe('it');
            $rootScope.$apply();
            expect($locale.id).toBe('es');
            expect($locale.DATETIME_FORMATS.DAY["0"]).toBe("Domingo");
            expect(callback.calls.count()).toBe(1);
          })
          .done();
        job.start();
      });
    });

    it('should do a deep copy of the locale elements', function(done) {
      inject(function($timeout, $locale, tmhDynamicLocale, tmhDynamicLocaleCache, $rootScope) {
        var job = createAsync(done);

        job
          .runs(function() {
            tmhDynamicLocale.set('es');
          })
          .waitsFor(function() {
            $timeout.flush(50);
            return $locale.id === 'es';
          }, 'locale not updated', 2000)
          .runs(function() {
            $locale.DATETIME_FORMATS.DAY["0"] = "XXX";
            expect($locale.DATETIME_FORMATS.DAY["0"]).not.toBe(tmhDynamicLocaleCache.get('es').DATETIME_FORMATS.DAY["0"]);
          })
          .done();
        job.start();
      });
     });

    it('should be able to handle locales with extra elements', function(done) {
      inject(function($timeout, $locale, tmhDynamicLocale, tmhDynamicLocaleCache, $rootScope) {
        var job = createAsync(done);
        var weirdLocale;

        job
          .runs(function() {
            tmhDynamicLocale.set('es');
          })
          .waitsFor(function() {
            $timeout.flush(50);
            return $locale.id === 'es';
          }, 'locale not updated', 2000)
          .runs(function() {
            weirdLocale = angular.copy($locale);
            weirdLocale.id = "xx";
            weirdLocale.EXTRA_PARAMETER = {foo: "FOO"};
            weirdLocale.DATETIME_FORMATS.DAY["7"] = "One More Day";
            tmhDynamicLocaleCache.put('xx', angular.copy(weirdLocale));
            tmhDynamicLocale.set('xx');
          })
          .waitsFor(function() {
            $timeout.flush(50);
            return $locale.id === 'xx';
          }, 'locale not updated', 2000)
          .runs(function() {
            expect($locale).toEqual(weirdLocale);
            expect($locale.EXTRA_PARAMETER).toEqual({foo: "FOO"});
            tmhDynamicLocale.set('es');
          })
          .waitsFor(function() {
            $timeout.flush(50);
            return $locale.id === 'es';
          }, 'locale not updated', 2000)
          .runs(function() {
            expect($locale.EXTRA_PARAMETER).toBeUndefined();
            expect($locale.DATETIME_FORMATS.DAY["7"]).toBeUndefined();
            expect($locale.DATETIME_FORMATS.DAY.length).toBe(7);
          })
          .done();
        job.start();
      });
    });

    describe('having a default locale', function() {
      beforeEach(module(function(tmhDynamicLocaleProvider) {
        tmhDynamicLocaleProvider.defaultLocale('it');
      }));
      it('should set the locale to the default locale', function(done) {
        inject(function($timeout, $locale, $rootScope) {
          var job = createAsync(done);

          job
            .runs(function() {
              expect($locale.id).toBe('en-us');
              $rootScope.$apply();
            })
            .waitsFor(function() {
              $timeout.flush(50);
              return $locale.id === 'it';
            }, 'locale not updated', 2000)
            .runs(function() {
              expect($locale.id).toBe('it');
            })
            .done();
          job.start();
        });
      });
    });

    describe('having a cookie storage', function () {
      beforeEach(module('ngCookies'));
      beforeEach(module(function(tmhDynamicLocaleProvider) {
        tmhDynamicLocaleProvider.useCookieStorage();
      }));

      it('should store the change on the cookie store', function(done) {
        inject(function ($timeout, $locale, $cookieStore, tmhDynamicLocale) {
          var job = createAsync(done);

          job
            .runs(function() {
              tmhDynamicLocale.set('es');
              expect($cookieStore.get('tmhDynamicLocale.locale')).toBe(undefined);
            })
            .waitsFor(function() {
              $timeout.flush(50);
              return $locale.id === 'es';
            }, 'locale not updated', 2000)
            .runs(function() {
              expect($cookieStore.get('tmhDynamicLocale.locale')).toBe('es');
            })
            .done();
          job.start();
        });
      });
      describe('reading the locale at initialization', function () {
        beforeEach(inject(function ($cookieStore, $rootScope) {
          $cookieStore.put('tmhDynamicLocale.locale', 'it');
          $rootScope.$apply();
        }));

        it('should load the locale on initialization', function(done) {
          inject(function ($timeout, $locale, $rootScope) {
            var job = createAsync(done);

            job
              .runs(function() {
                expect($locale.id).toBe('en-us');
              })
              .waitsFor(function() {
                $timeout.flush(50);
                return $locale.id === 'it';
              }, 'locale not updated', 2000)
              .runs(function() {
                expect($locale.id).toBe('it');
              })
              .done();
            job.start();
          });
        });
      });
      describe('and having a default language', function () {
        beforeEach(module(function(tmhDynamicLocaleProvider) {
          tmhDynamicLocaleProvider.defaultLocale('es');
        }));
        beforeEach(inject(function ($cookieStore, $rootScope) {
          $cookieStore.put('tmhDynamicLocale.locale', 'it');
          $rootScope.$apply();
        }));

        it('should load the locale on initialization', function(done) {
          inject(function ($timeout, $locale, $rootScope) {
            var job = createAsync(done);

            job
              .runs(function() {
                expect($locale.id).toBe('en-us');
              })
              .waitsFor(function() {
                $timeout.flush(50);
                return $locale.id === 'it';
              }, 'locale not updated', 2000)
              .runs(function() {
                expect($locale.id).toBe('it');
              })
              .done();
            job.start();
          });
        });
      });
      describe('and changing the name of the storageKey', function () {
        beforeEach(module(function(tmhDynamicLocaleProvider) {
          tmhDynamicLocaleProvider.storageKey('customStorageKeyName');
        }));

        it('should change the name of the storageKey', function(done) {
          inject(function ($timeout, $locale, $cookieStore, tmhDynamicLocale) {
            var job = createAsync(done);

            job
            .runs(function() {
              tmhDynamicLocale.set('es');
              expect($cookieStore.get('customStorageKeyName')).toBe(undefined);
              expect($cookieStore.get('tmhDynamicLocale.locale')).toBe(undefined);
            })
            .waitsFor(function() {
              $timeout.flush(50);
              return $locale.id === 'es';
            }, 'locale not updated', 2000)
            .runs(function() {
              expect($cookieStore.get('tmhDynamicLocale.locale')).toBe(undefined);
              expect($cookieStore.get('customStorageKeyName')).toBe('es');
            })
            .done();
            job.start();
          });
        });
      });
    });

    describe('loading locales using <script>', function () {
      function countLocales($document, localeId) {
        var count = 0,
          scripts = $document[0].getElementsByTagName('script');

        for (var i = 0; i < scripts.length; ++i) {
          count += (scripts[i].src === 'http://localhost:9876/base/node_modules/angular-i18n/angular-locale_' + localeId + '.js' ? 1 : 0);
        }
        return count;
      }

      it('should load the locales using a <script> tag', function(done) {
        inject(function ($timeout, tmhDynamicLocale, $document, $locale) {
          var job = createAsync(done);
          job
            .runs(function() {
              tmhDynamicLocale.set('fr');
              expect(countLocales($document, 'fr')).toBe(1);
            })
            .waitsFor(function() {
              $timeout.flush(50);
              return $locale.id === 'fr';
            }, 'locale not updated', 2000)
            .runs(function() {
              expect(countLocales($document, 'fr')).toBe(0);
            })
            .done();
        job.start();
        });
      });

      it('should not load the same locale twice', function(done) {
        inject(function ($timeout, tmhDynamicLocale, $rootScope, $document, $locale) {
          var job = createAsync(done);

          job
            .runs(function() {
              tmhDynamicLocale.set('ja');
              tmhDynamicLocale.set('ja');
              expect(countLocales($document, 'ja')).toBe(1);
            })
            .waitsFor(function() {
              $timeout.flush(50);
              return $locale.id === 'ja';
            }, 'locale not updated', 2000)
            .runs(function() {
              expect(countLocales($document, 'ja')).toBe(0);
              tmhDynamicLocale.set('ja');
              expect(countLocales($document, 'ja')).toBe(0);
              tmhDynamicLocale.set('et');
            })
            .waitsFor(function() {
              $timeout.flush(50);
              return $locale.id === 'et';
            }, 'locale not updated', 2000)
            .runs(function() {
              $rootScope.$apply(function () {
                tmhDynamicLocale.set('ja');
                expect(countLocales($document, 'ja')).toBe(0);
              });
              expect(countLocales($document, 'ja')).toBe(0);
            })
            .done();
          job.start();
        });
      });

      it('should return a promise that is resolved when the script is loaded', function(done) {
        inject(function ($timeout, tmhDynamicLocale, $document, $locale) {
          var job = createAsync(done);
          var callback = jasmine.createSpy();

          job
            .runs(function() {
              tmhDynamicLocale.set('ko').then(callback);
              tmhDynamicLocale.set('ko').then(callback);
              expect(callback).not.toHaveBeenCalled();
            })
            .waitsFor(function() {
              $timeout.flush(50);
              return $locale.id === 'ko';
            }, 'locale not updated', 2000)
            .runs(function() {
              expect(callback.calls.count()).toBe(2);
            })
            .done();
          job.start();
        });
      });
    });
  });
}());
