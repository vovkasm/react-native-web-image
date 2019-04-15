'use strict'
const path = require('path')

module.exports = {
  projectRoot: path.resolve(__dirname, 'samples/SampleApp'),
  watchFolders: [__dirname],

  resolver: {
    extraNodeModules: {
      'react-native-web-image': __dirname,
    }
  }
}
