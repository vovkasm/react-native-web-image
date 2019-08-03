import React from 'react'
import { ScrollView, StyleSheet, View } from 'react-native'

import WebImage from 'react-native-web-image'

import { testUrl } from './testUrl'

const TestDefaultFlexible: React.FC = () => {
  const uri = testUrl(100, 80)
  return (
    <ScrollView style={s.container}>
      <View style={[s.block, { width: 102, height: 82 }]}>
        <WebImage source={{ uri }} style={s.web} />
      </View>
      <View style={[s.block, { width: 120, height: 120 }]}>
        <WebImage source={{ uri }} style={s.web} />
      </View>
      <View style={[s.block, { width: 120, height: 90 }]}>
        <WebImage source={{ uri }} style={s.web} />
      </View>
      <View style={[s.block, { width: 90, height: 90 }]}>
        <WebImage source={{ uri }} style={s.web} />
      </View>
      <View style={[s.block, { width: 90, height: 50 }]}>
        <WebImage source={{ uri }} style={s.web} />
      </View>
    </ScrollView>
  )
}

const s = StyleSheet.create({
  block: {
    borderColor: 'green',
    borderWidth: 1,
  },
  container: {
    flex: 1,
  },
  web: {
    flex: 1,
  },
})

export default TestDefaultFlexible
