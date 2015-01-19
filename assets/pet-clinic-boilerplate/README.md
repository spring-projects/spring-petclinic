Pet Clinic Grunt Boilerplate
====================

Pet Clinic Grunt Boilerplate is the static file generator for [Spring Pet Clinic repo](https://github.com/singularity-sg/spring-petclinic), plus a bit more. It is based on Cris Coyer's [My-Grunt-Boilerplate](https://github.com/chriscoyier/My-Grunt-Boilerplate) repo.

### Start here if you are using a GUI client:

Run `npm install` to install all dependencies.

## Compiling CSS and JavaScript

Pet Clinic Grunt Boilerplate uses [Grunt](http://gruntjs.com/) with convenient methods for working with the framework. It's how we compile our code, run tests, and more. To use it, install the required dependencies as directed and then run some Grunt commands.

### Install Grunt

From the command line:

1. Install `grunt-cli` globally with `npm install -g grunt-cli`.
2. Navigate to the root `/pet-clinic-boilerplate` directory, then run `npm install`. npm will look at [package.json](https://github.com/twbs/bootstrap/blob/master/package.json) and automatically install the necessary local dependencies listed there.

When completed, you'll be able to run the various Grunt commands provided from the command line.

**Unfamiliar with `npm`? Don't have node installed?** That's a-okay. npm stands for [node packaged modules](http://npmjs.org/) and is a way to manage development dependencies through node.js. [Download and install node.js](http://nodejs.org/download/) before proceeding.

### Available Grunt commands

#### Build Development code - `grunt`
Run `grunt` to run tests locally and compile the CSS and JavaScript into `/dist`. Once you see a "Done, without errors." after running `grunt` on commandline, you should be cool as everything works.

#### Build Production code - `grunt dist`
`grunt dist` creates the `/dist` directory with compiled files. **Uses [SASS](http://sass-lang.com/) and [UglifyJS](http://lisperator.net/uglifyjs/).**

#### Watch - `grunt watch`
This is a convenience method for watching SASS & JavaScript files and automatically building them whenever you save.

#### Connect - `grunt connect::keepalive`
This is a convenience method that let's you run a local server with default URL address on http://0.0.0.0:8000

### Troubleshooting dependencies

Should you encounter problems with installing dependencies or running Grunt commands, uninstall all previous dependency versions (global and local). Then, re-run `npm install`.
