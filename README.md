# React Native WebImage

An image component for react-native with persistent disk and memory caching.

It is mostly a wrapper around native libraries which actually do the hard work.
On Android it uses [Glide](https://github.com/bumptech/glide), on iOS –
[SDWebImage](https://github.com/rs/SDWebImage).

:warning: The package is currently in alpha stage of development. If you find a bug or missing functionality, please feel free to report, but better fix or implement what you want and send a pull request to GitHub repository.

## Installation

```sh
npm install --save react-native-web-image
react-native link
```

## Usage

```javascript
import React, { Component } from 'react'
import { StyleSheet, Text, View } from 'react-native'
import WebImage from 'react-native-web-image'

export default class App extends Component {
  render() {
    const imageUri = 'https://placeholdit.imgix.net/~text?txtsize=33&txt=200x150&w=200&h=150'
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>Sample image</Text>
        <View style={styles.imgContainer}>
          <WebImage style={styles.img} source={{uri:imageUri}}/>
        </View>
      </View>
    )
  }
}

const white = '#FFFFFF'
const blue = 'rgb(0,0,255)'
const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: white,
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  img: {
    flex: 1,
  },
  imgContainer: {
    flexDirection: 'row',
    flex: 1,
    borderWidth: 1,
    borderColor: blue,
  }
})
```

## Author

[Vladimir Timofeev](https://github.com/vovkasm)

## License

* Main source code is licensed under the MIT License.
* SDWebImage (embedded in project) is licensed under the MIT License.
* Glide (included via gradle) is licensed under Apache 2.0 License.
