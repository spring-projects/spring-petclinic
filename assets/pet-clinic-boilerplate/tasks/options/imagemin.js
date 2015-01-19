module.exports = {
  dynamic: {
    files: [{
      expand: true,
      cwd: 'images/',
      src: ['**/*.{png,jpg,gif}'],
      dest: 'images/'
    }]
  }
}