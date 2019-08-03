import React from 'react'
import { StyleProp, StyleSheet, Text, View, ViewStyle } from 'react-native'

import WebImage from 'react-native-web-image'

interface IProps {
  style?: StyleProp<ViewStyle>
}

const TestItem: React.FC<IProps> = (props) => {
  const uri = 'https://via.placeholder.com/100x80.png?text=100x80'
  const style = StyleSheet.flatten([{ flex: 1 }, props.style])
  return (
    <View style={s.test}>
      <View style={s.imageBlock}>
        <WebImage source={{ uri }} style={style} />
      </View>
      <View style={s.testDescription}>
        <Text style={s.testDescriptionText}>{JSON.stringify(style, null, ' ')}</Text>
      </View>
    </View>
  )
}

const s = StyleSheet.create({
  imageBlock: {
    borderColor: '#ccc',
    borderWidth: StyleSheet.hairlineWidth,
    height: 160,
    width: 160,
  },
  test: {
    borderBottomWidth: StyleSheet.hairlineWidth,
    borderColor: '#ccc',
    flexDirection: 'row',
    padding: 2,
  },
  testDescription: {
    flex: 1,
    padding: 2,
  },
  testDescriptionText: {
    fontSize: 8,
  },
})

export default TestItem
