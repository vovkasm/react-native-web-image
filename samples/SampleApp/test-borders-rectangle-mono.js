import React from 'react'
import { ScrollView, StyleSheet, View } from 'react-native'
import WebImage from 'react-native-web-image'

export default function TestBordersRectangleMono () {
  const uri = 'https://placeholdit.imgix.net/~text?txtsize=26&txt=100x80&w=100&h=80'
  const bs = {
    borderColor: 'red',
    borderWidth: 1
  }
  return <ScrollView style={s.container}>
    <View style={[s.block, {width: 160, height: 160}]}><WebImage source={{uri}} style={[s.web, bs]} /></View>
    <View style={[s.block, {width: 160, height: 160}]}><WebImage source={{uri}} style={[s.web, bs, {borderWidth: 20}]} /></View>
    <View style={[s.block, {width: 160, height: 160}]}><WebImage source={{uri}} style={[s.web, bs, {borderTopWidth: 1, borderBottomWidth: 1, borderLeftWidth: 20, borderRightWidth: 20}]} /></View>
    <View style={[s.block, {width: 160, height: 160}]}><WebImage source={{uri}} style={[s.web, bs, {borderTopWidth: 0, borderBottomWidth: 8, borderLeftWidth: 2, borderRightWidth: 2}]} /></View>
  </ScrollView>
}

const s = StyleSheet.create({
  container: {
    flex: 1
  },
  block: {
    borderWidth: 1,
    borderColor: 'green',
    margin: 1
  },
  web: {
    flex: 1
  }
})
