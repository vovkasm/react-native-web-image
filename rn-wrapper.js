
const path = require('path')

// modify paths to be absolute
const PATH_ARG_RE = /^--?(root|projectRoots)$/
const argv = process.argv
for (let i = 2; i < argv.length; ++i) {
  if (PATH_ARG_RE.test(argv[i - 1])) {
    argv[i] = argv[i].split(',').map(function (p) {
      return path.resolve(__dirname, p)
    }).join(',')
  }
}

const cli = require('react-native/cli')
cli.run()
