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

const Samples: React.FC = (props) => {
  return (
    <ScrollView>
      <Sample title="[200x150] in [auto x 120] block" source={{ uri: testUrl(200, 150) }} height={120} />
      <Sample title="[200x150] in [100 x 100] block" source={{ uri: testUrl(200, 150) }} height={100} width={100} />
      <Sample
        title="[200x150] in [auto x 120] block, resizeMode=cover"
        source={{ uri: testUrl(200, 150) }}
        resizeMode="cover"
        height={120}
      />
      <Sample
        title="[200x150] in [100 x 100] block, resizeMode=cover"
        source={{ uri: testUrl(200, 150) }}
        resizeMode="cover"
        height={100}
        width={100}
      />
      <Sample
        title="[200x150] in [auto x 120] block, resizeMode=stretch"
        source={{ uri: testUrl(200, 150) }}
        resizeMode="stretch"
        height={120}
      />
      <Sample
        title="[200x150] in [100 x 100] block, resizeMode=stretch"
        source={{ uri: testUrl(200, 150) }}
        resizeMode="stretch"
        height={100}
        width={100}
      />
      <Sample
        title="[200x150] in [auto x 120] block, resizeMode=center"
        source={{ uri: testUrl(200, 150) }}
        resizeMode="center"
        height={120}
      />
      <Sample
        title="[200x150] in [100 x 100] block, resizeMode=center"
        source={{ uri: testUrl(200, 150) }}
        resizeMode="center"
        height={100}
        width={100}
      />
    </ScrollView>
  )
}

export default Samples

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
