import React from 'react'
import { ScrollView, StyleSheet, View } from 'react-native'
import WebImage from 'react-native-web-image'

export default function TestBordersFlexible () {
  const uri = 'https://placeholdit.imgix.net/~text?txtsize=26&txt=100x80&w=100&h=80'
  const bs = {
    borderColor: 'red',
    borderWidth: 2,
    borderRadius: 10
  }
  return <ScrollView style={s.container}>
    <View style={[s.block, {width: 102 + 4, height: 82 + 4}]}><WebImage source={{uri}} style={[s.web, bs]} /></View>
    <View style={[s.block, {width: 120 + 4, height: 120 + 4}]}><WebImage source={{uri}} style={[s.web, bs]} /></View>
    <View style={[s.block, {width: 120 + 4, height: 90 + 4}]}><WebImage source={{uri}} style={[s.web, bs]} /></View>
    <View style={[s.block, {width: 90 + 4, height: 90 + 4}]}><WebImage source={{uri}} style={[s.web, bs]} /></View>
    <View style={[s.block, {width: 90 + 4, height: 50 + 4}]}><WebImage source={{uri}} style={[s.web, bs]} /></View>
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
