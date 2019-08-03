import PropTypes from 'prop-types'
import React from 'react'
import { ScrollView, StyleSheet, Text, View } from 'react-native'
import WebImage from 'react-native-web-image' // 'react-native-web-image' sources here

import { testUrl } from './testUrl'

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

const Samples = (props) => {
  return <ScrollView>
    <Sample
      title='[200x150] in [auto x 120] block'
      source={{uri: testUrl(200,150)}}
      height={120}
    />
    <Sample
      title='[200x150] in [100 x 100] block'
      source={{uri: testUrl(200,150)}}
      height={100} width={100}
    />
    <Sample
      title='[200x150] in [auto x 120] block, resizeMode=cover'
      source={{uri: testUrl(200,150)}}
      resizeMode='cover'
      height={120}
    />
    <Sample
      title='[200x150] in [100 x 100] block, resizeMode=cover'
      source={{uri: testUrl(200,150)}}
      resizeMode='cover'
      height={100} width={100}
    />
    <Sample
      title='[200x150] in [auto x 120] block, resizeMode=stretch'
      source={{uri: testUrl(200,150)}}
      resizeMode='stretch'
      height={120}
    />
    <Sample
      title='[200x150] in [100 x 100] block, resizeMode=stretch'
      source={{uri: testUrl(200,150)}}
      resizeMode='stretch'
      height={100} width={100}
    />
    <Sample
      title='[200x150] in [auto x 120] block, resizeMode=center'
      source={{uri: testUrl(200,150)}}
      resizeMode='center'
      height={120}
    />
    <Sample
      title='[200x150] in [100 x 100] block, resizeMode=center'
      source={{uri: testUrl(200,150)}}
      resizeMode='center'
      height={100} width={100}
    />
  </ScrollView>
}

export default Samples

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
