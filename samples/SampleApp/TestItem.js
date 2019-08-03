import PropTypes from 'prop-types'
import React from 'react'
import { StyleSheet, Text, View } from 'react-native'
import WebImage from 'react-native-web-image'

export default function TestItem (props) {
  const uri = 'https://via.placeholder.com/100x80.png?text=100x80'
  const style = {
    flex: 1,
    ...props.style
  }
  return <View style={s.test}>
    <View style={s.imageBlock}>
      <WebImage source={{uri}} style={style} />
    </View>
    <View style={s.testDescription}>
      <Text style={s.testDescriptionText}>{JSON.stringify(style, null, ' ')}</Text>
    </View>
  </View>
}
TestItem.propTypes = {
  style: PropTypes.object
}

const s = StyleSheet.create({
  test: {
    flexDirection: 'row',
    borderBottomWidth: StyleSheet.hairlineWidth,
    borderColor: '#ccc',
    padding: 2
  },
  imageBlock: {
    borderWidth: StyleSheet.hairlineWidth,
    borderColor: '#ccc',
    width: 160,
    height: 160
  },
  testDescription: {
    flex: 1,
    padding: 2
  },
  testDescriptionText: {
    fontSize: 8
  }
})
