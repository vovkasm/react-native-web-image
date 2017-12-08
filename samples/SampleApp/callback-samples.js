import PropTypes from 'prop-types'
import React from 'react'
import { ScrollView, StyleSheet, Text, View } from 'react-native'
import WebImage from 'react-native-web-image' // 'react-native-web-image' sources here

const Sample = (props) => {
  const { title, width, height, ...other } = props
  const containerStyle = {}
  if (width !== undefined) {
    containerStyle.width = width
  }
  if (height !== undefined) {
    containerStyle.height = height
  }
  return <View style={styles.container}>
    <Text style={styles.welcome}>{title}</Text>
    <View style={[styles.imgContainer, containerStyle]}>
      <WebImage style={styles.img} {...other} />
    </View>
  </View>
}
Sample.propTypes = {
  title: PropTypes.string.isRequired,
  width: PropTypes.number,
  height: PropTypes.number,
  resizeMode: PropTypes.string
}

const CallbackSamples = (props) => {
  return <ScrollView>
    <Sample
      title='Image with success and error blocks'
      source={{uri: 'https://placeholdit.imgix.net/~text?txtsize=33&txt=Sample&w=200&h=150'}}
      height={120}
      onError={(e) => { console.log('Image 1 onError handler: ', e) }}
      onLoad={(e) => { console.log('Image 1 onLoad handler: ', e.nativeEvent) }}
      />
    <Sample
      title='image with onError handler, see logs'
      source={{uri: 'https://httpbin.org/status/404'}}
      height={100}
      onError={(e) => { console.log('Image 2 onError handler: ', e) }}
      onLoad={(e) => { console.log('Image 2 onLoad handler: ', e.nativeEvent) }}
      />
  </ScrollView>
}

export default CallbackSamples

const styles = StyleSheet.create({
  container: {
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#FFFFFF'
  },
  welcome: {
    fontSize: 12,
    textAlign: 'center',
    margin: 10
  },
  img: {
    flex: 1,
    borderColor: 'rgb(127,255,127)'
  },
  imgContainer: {
    flexDirection: 'row',
    borderWidth: 1,
    borderColor: 'rgb(0,0,255)'
  }
})
