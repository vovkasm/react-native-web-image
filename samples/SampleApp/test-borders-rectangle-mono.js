import React, { PropTypes } from 'react'
import { ScrollView, StyleSheet, Text, View } from 'react-native'
import WebImage from 'react-native-web-image'

function TestItem (props) {
  const uri = 'https://placeholdit.imgix.net/~text?txtsize=26&txt=100x80&w=100&h=80'
  const style = {
    flex: 1,
    ...props.style
  }
  return <View style={s.test}>
    <View style={[s.block, {width: 160, height: 160}]}>
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

export default function TestBordersRectangleMono () {
  const bs = { borderColor: 'red', borderWidth: 1 }
  return <ScrollView style={s.container} contentContainerStyle={s.content}>
    <TestItem style={bs} />
    <TestItem style={{ ...bs, borderWidth: 20 }} />
    <TestItem style={{ ...bs, borderTopWidth: 1, borderBottomWidth: 1, borderLeftWidth: 20, borderRightWidth: 20 }} />
    <TestItem style={{ ...bs, borderTopWidth: 0, borderBottomWidth: 8, borderLeftWidth: 2, borderRightWidth: 2 }} />
  </ScrollView>
}

const s = StyleSheet.create({
  container: {
    flex: 1
  },
  content: {
    width: 600
  },
  test: {
    flexDirection: 'row',
    borderBottomWidth: StyleSheet.hairlineWidth,
    borderColor: '#ccc',
    padding: 2
  },
  block: {
    borderWidth: 1,
    borderColor: 'green'
  },
  testDescription: {
    flex: 1,
    padding: 2
  },
  testDescriptionText: {
    fontSize: 8
  }
})
