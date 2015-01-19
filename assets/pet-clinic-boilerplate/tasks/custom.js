module.exports = function(grunt) {
  grunt.registerTask('custom', 'Say hello!', function() {
    grunt.log.writeln("Custom task log");
  });
};