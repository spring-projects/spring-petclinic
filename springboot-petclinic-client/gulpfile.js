var gulp = require('gulp');
var cleanCSS = require('gulp-clean-css');
var uglify = require('gulp-uglify');
var less = require('gulp-less');

var paths = {
    "css"    : "src/css/*",
    "fonts"  : "src/fonts/*",
    "images" : "src/images/*",
    "html"   : "src/scripts/**/*.html",
    "js"     : "src/scripts/**/*.js",
    "less"   : "src/less/*",
    "dist"   : "target/dist/"
};

gulp.task('minify-css', function() {
    return gulp.src(paths.css)
        .pipe(cleanCSS())
        .pipe(gulp.dest(paths.dist + 'css/'));
});

gulp.task('minify-js', function() {
    return gulp.src(paths.js)
        .pipe(uglify())
        .pipe(gulp.dest(paths.dist + 'scripts/'));
});

gulp.task('less', function () {
    return gulp.src(paths.less)
        .pipe(less())
        .pipe(gulp.dest(paths.dist + 'css/'));
});

gulp.task('copy-fonts', function() {
    return gulp.src(paths.fonts)
        .pipe(gulp.dest(paths.dist + 'fonts/'))
});

gulp.task('copy-html', function() {
    return gulp.src(paths.html)
        .pipe(gulp.dest(paths.dist + 'scripts/'))
});

gulp.task('copy-images', function() {
    return gulp.src(paths.images)
        .pipe(gulp.dest(paths.dist + 'images/'))
});

gulp.task('default', ['minify-css', 'minify-js', 'less',
          'copy-fonts', 'copy-html', 'copy-images'], function() {});
