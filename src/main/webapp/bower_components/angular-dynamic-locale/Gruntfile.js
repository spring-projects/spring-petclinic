(function () {
  'use strict';

  module.exports = function(grunt) {
    //grunt plugins
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-jscs');
    grunt.loadNpmTasks('grunt-karma');
    grunt.loadNpmTasks('grunt-bump');
    grunt.loadNpmTasks('grunt-npm');
    grunt.loadNpmTasks('grunt-contrib-uglify');

    grunt.initConfig({
      jshint: {
        all: ['Gruntfile.js', 'src/*.js', 'test/*.js']
      },
      jscs: {
        src: ['src/**/*.js', 'test/**/*.js'],
        options: {
          config: ".jscs.json"
        }
      },
      karma: {
        unit: { configFile: 'karma.conf.js' },
        'unit.min': {
          configFile: 'karma.min.conf.js'
        },
        autotest: {
          configFile: 'karma.conf.js',
          autoWatch: true,
          singleRun: false
        },
        travis: {
          configFile: 'karma.conf.js',
          reporters: 'dots',
          browsers: ['PhantomJS']
        }
      },
      uglify: {
        all: {
          files: {
            'tmhDynamicLocale.min.js': ['src/*.js']
          },
          options: {
            sourceMap: true
          }
        }
      },
      bump: {
        options: {
          files: ['package.json', 'bower.json'],
          commitFiles: ['package.json', 'bower.json'],
          tagName: '%VERSION%',
          pushTo: 'origin'
        }
      },
      'npm-publish': {
         options: {
           requires: ['jshint', 'karma:unit', 'bump'],
           abortIfDirty: true
         }
      }
    });
    grunt.registerTask('release', ['jshint', 'jscs', 'karma:unit', 'uglify:all', 'karma:unit.min', 'bump', 'publish']);
  };
}());

