import React from 'react'
import { ScrollView, StyleSheet, View } from 'react-native'
import WebImage from 'react-native-web-image'

import { testUrl } from './testUrl'

export default function BenchNoDecor () {
  const uri = testUrl(100, 80)
  const comps = []
  for (let i = 0; i < 200; i++) {
    comps.push(<View key={`k-${i}`} style={s.block}><WebImage source={{uri}} style={s.web} /></View>)
  }
  return <ScrollView style={s.container}>{ comps }</ScrollView>
}

const s = StyleSheet.create({
  container: {
    flex: 1
  },
  block: {
    width: 90,
    height: 90,
    borderWidth: 1,
    borderColor: 'green'
  },
  web: {
    flex: 1
  }
})
