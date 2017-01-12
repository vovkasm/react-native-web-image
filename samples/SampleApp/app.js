import React, { PropTypes } from 'react'
import { StyleSheet, Text, View, ScrollView } from 'react-native'
import WebImage from 'react-native-web-image' // 'react-native-web-image' sources here

const Sample = (props) => {
  const { width, height, ...other } = props
  const containerStyle = {}
  if (width !== undefined) {
    containerStyle.width = width
  }
  if (height !== undefined) {
    containerStyle.height = height
  }
  let title = 'Sample ' + (width !== undefined ? width : 'auto') + ' x ' + (height !== undefined ? height : 'auto')
  if (props.resizeMode !== undefined) {
    title += ' ' + props.resizeMode
  }
  return <View style={styles.container}>
    <Text style={styles.welcome}>{title}</Text>
    <View style={[styles.imgContainer, containerStyle]}>
      <WebImage style={styles.img} {...other} />
    </View>
  </View>
}
Sample.propTypes = {
  width: PropTypes.number,
  height: PropTypes.number,
  resizeMode: PropTypes.string
}

export default class App extends React.Component {
  render () {
    return <ScrollView>
      <Sample
        source={{uri: 'https://placeholdit.imgix.net/~text?txtsize=33&txt=200x150&w=200&h=150'}}
        height={120}
      />
      <Sample
        source={{uri: 'https://placeholdit.imgix.net/~text?txtsize=33&txt=200x150&w=200&h=150'}}
        height={100} width={100}
      />
      <Sample
        source={{uri: 'https://placeholdit.imgix.net/~text?txtsize=33&txt=200x150&w=200&h=150'}}
        resizeMode='cover'
        height={120}
      />
      <Sample
        source={{uri: 'https://placeholdit.imgix.net/~text?txtsize=33&txt=200x150&w=200&h=150'}}
        resizeMode='cover'
        height={100} width={100}
      />
      <Sample
        source={{uri: 'https://placeholdit.imgix.net/~text?txtsize=33&txt=200x150&w=200&h=150'}}
        resizeMode='stretch'
        height={120}
      />
      <Sample
        source={{uri: 'https://placeholdit.imgix.net/~text?txtsize=33&txt=200x150&w=200&h=150'}}
        resizeMode='stretch'
        height={100} width={100}
      />
      <Sample
        source={{uri: 'https://placeholdit.imgix.net/~text?txtsize=33&txt=200x150&w=200&h=150'}}
        resizeMode='center'
        height={120}
      />
      <Sample
        source={{uri: 'https://placeholdit.imgix.net/~text?txtsize=33&txt=200x150&w=200&h=150'}}
        resizeMode='center'
        height={100} width={100}
      />
    </ScrollView>
  }
}

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
    flex: 1
  },
  imgContainer: {
    flexDirection: 'row',
    borderWidth: 1,
    borderColor: 'rgb(0,0,255)'
  }
})
