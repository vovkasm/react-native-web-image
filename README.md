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

| version       | react-native |
| ------------- | ------------ |
| 0.0.5 - 0.0.6 | &gt;=0.47.0  |
| 0.0.3 - 0.0.4 | &gt;=0.40.0  |
| &lt;=0.0.2    | &lt;0.40.0 (tested with 0.37.0) |

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

## API

### WebImage element

```javascript
<WebImage source={source}/>
```

| Attribute  | Type     | Description |
| ---------- | -------- | ----------- |
| source     | Object   | Describes image source (mimics original Image element) |
| source.uri | String   | (**Required**) URL of the image |
| resizeMode | Enum{'cover','contain','stretch','center'} | Determine resize mode for image. Default: 'contain' |
| onError    | Function | Will be called on error |
| onLoad     | Function | Will be called when loading of image completed |

#### Resize modes

* **contain** - Scale the image uniformly (maintain the image's aspect ratio)
  so that both dimensions (width and height) of the image will be equal to or
  less than the corresponding dimension of the view (minus padding).
* **cover** - Scale the image uniformly so that both dimensions (width and
  height) of the image will be equal to or larger than the corresponding
  dimension of the view (minus padding).
* **stretch** - Scale width and height independently, aspect ration will not be
  preserved.
* **center** - Scale the image down so that it is completely visible, if bigger
  than the area of the view. The image will not be scaled up. The image will be
  placed at the center of the view.

#### onError(event)

* `event.nativeEvent.error` - String representation of error (platform dependent)
* `event.nativeEvent.uri` - URI which leads to error

#### onLoad(event)

* `event.nativeEvent.source.uri` - URI of the loaded image
* `event.nativeEvent.source.width` - width of the loaded image
* `event.nativeEvent.source.height` - height of the loaded image

## Author

[Vladimir Timofeev](https://github.com/vovkasm)

## Contributors

* [iisue](https://github.com/iisue)

## License

* Main source code is licensed under the MIT License.
* SDWebImage (embedded in project) is licensed under the MIT License.
* Glide (included via gradle) is licensed under Apache 2.0 License.
