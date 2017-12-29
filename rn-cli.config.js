'use strict'
const path = require('path')

module.exports = {
  extraNodeModules: {
    'react-native-web-image': __dirname
  },
  getProjectRoots: function () {
    return [path.resolve(__dirname, 'samples/SampleApp'), path.resolve(__dirname, '.')]
  }
}
