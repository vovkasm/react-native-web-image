import React from 'react'
import { ScrollView, StyleSheet, View } from 'react-native'
import WebImage from 'react-native-web-image'

export default function TestDefaultFlexible () {
  const uri = 'https://placeholdit.imgix.net/~text?txtsize=26&txt=100x80&w=100&h=80'
  return <ScrollView style={s.container}>
    <View style={[s.block, {width: 102, height: 82}]}><WebImage source={{uri}} style={s.web} /></View>
    <View style={[s.block, {width: 120, height: 120}]}><WebImage source={{uri}} style={s.web} /></View>
    <View style={[s.block, {width: 120, height: 90}]}><WebImage source={{uri}} style={s.web} /></View>
    <View style={[s.block, {width: 90, height: 90}]}><WebImage source={{uri}} style={s.web} /></View>
    <View style={[s.block, {width: 90, height: 50}]}><WebImage source={{uri}} style={s.web} /></View>
  </ScrollView>
}

const s = StyleSheet.create({
  container: {
    flex: 1
  },
  block: {
    borderWidth: 1,
    borderColor: 'green'
  },
  web: {
    flex: 1
  }
})
