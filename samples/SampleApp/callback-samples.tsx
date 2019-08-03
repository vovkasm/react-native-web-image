// tslint:disable: no-console

import React from 'react'
import { ScrollView, StyleSheet, Text, View, ViewStyle } from 'react-native'
import WebImage, { IWebImageProps } from 'react-native-web-image'

import { testUrl } from './testUrl'

interface ISampleProps extends IWebImageProps {
  title: string
  width?: number
  height?: number
}
const Sample: React.FC<ISampleProps> = (props) => {
  const { title, width, height, ...other } = props
  const containerStyle: ViewStyle = {}
  if (width !== undefined) {
    containerStyle.width = width
  }
  if (height !== undefined) {
    containerStyle.height = height
  }
  return (
    <View style={styles.container}>
      <Text style={styles.welcome}>{title}</Text>
      <View style={[styles.imgContainer, containerStyle]}>
        <WebImage style={styles.img} {...other} />
      </View>
    </View>
  )
}

const CallbackSamples: React.FC<{}> = (props) => {
  return (
    <ScrollView>
      <Sample
        title="Image with success and error blocks"
        source={{ uri: testUrl(200, 150) }}
        height={120}
        onError={(e) => {
          console.log('Image 1 onError handler: ', e.nativeEvent)
        }}
        onLoad={(e) => {
          console.log('Image 1 onLoad handler: ', e.nativeEvent)
        }}
      />
      <Sample
        title="image with onError handler, see logs"
        source={{ uri: 'https://httpbin.org/status/404' }}
        height={100}
        onError={(e) => {
          console.log('Image 2 onError handler: ', e.nativeEvent)
        }}
        onLoad={(e) => {
          console.log('Image 2 onLoad handler: ', e.nativeEvent)
        }}
      />
    </ScrollView>
  )
}

export default CallbackSamples

const styles = StyleSheet.create({
  container: {
    alignItems: 'center',
    backgroundColor: '#FFFFFF',
    justifyContent: 'center',
  },
  img: {
    borderColor: 'rgb(127,255,127)',
    flex: 1,
  },
  imgContainer: {
    borderColor: 'rgb(0,0,255)',
    borderWidth: 1,
    flexDirection: 'row',
  },
  welcome: {
    fontSize: 12,
    margin: 10,
    textAlign: 'center',
  },
})
