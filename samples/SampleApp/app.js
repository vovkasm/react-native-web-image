import React, { PropTypes } from 'react'
import { Button, ScrollView, StyleSheet, Text, View } from 'react-native'

import Samples from './samples'
import TestDefault from './test-default'
import TestBorders from './test-borders'

const Main = ({ navigateTo }) => {
  return <ScrollView>
    <Button title='Samples' onPress={() => { navigateTo('/samples') }} />
    <Button title='Default params' onPress={() => { navigateTo('/test-default') }} />
    <Button title='Borders' onPress={() => { navigateTo('/test-borders') }} />
  </ScrollView>
}
Main.propTypes = {
  navigateTo: PropTypes.func.isRequired
}

const routes = {
  '/': Main,
  '/samples': Samples,
  '/test-default': TestDefault,
  '/test-borders': TestBorders
}

export default class App extends React.Component {
  constructor (props, ctx) {
    super(props, ctx)
    this.state = {
      route: '/'
    }
  }
  navigateTo = (route) => {
    this.setState({ route })
  }
  render () {
    const route = this.state.route
    const comp = routes[route]
    if (comp === undefined) {
      throw new Error(`Component for '${route}' not found`)
    }
    return <View style={s.screen}>
      {this.renderHeaderFor(comp)}
      <View style={s.scene}>{this.renderSceneComp(comp)}</View>
    </View>
  }
  renderSceneComp (comp) {
    return React.createElement(comp, { navigateTo: this.navigateTo }, null)
  }
  renderHeaderFor (comp) {
    const title = comp.title || comp.name || comp.className
    return <View style={s.header}>
      <Button title='Back' onPress={() => { this.navigateTo('/') }} /><Text>{title}</Text>
    </View>
  }
}

const s = StyleSheet.create({
  screen: {
    flex: 1
  },
  scene: {
    flex: 1
  },
  header: {
    marginTop: 15,
    flexDirection: 'row',
    alignItems: 'center'
  }
})
