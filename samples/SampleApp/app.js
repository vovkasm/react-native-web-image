import React, { PropTypes } from 'react'
import { Button, ScrollView, StyleSheet, Text, View } from 'react-native'

import Samples from './samples'
import TestDefault from './test-default'
import TestBorders from './test-borders'

const TestItem = ({ title, onPress }) => {
  return <View style={s.testItem}><Button title={title} onPress={onPress} /></View>
}
TestItem.propTypes = {
  onPress: PropTypes.func.isRequired,
  title: PropTypes.string.isRequired
}

const Main = ({ navigateTo }) => {
  return <ScrollView>
    <TestItem title='Samples' onPress={() => { navigateTo('/samples') }} />
    <TestItem title='Default params' onPress={() => { navigateTo('/test-default') }} />
    <TestItem title='Borders' onPress={() => { navigateTo('/test-borders') }} />
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
    marginBottom: 2,
    marginHorizontal: 8,    
    flexDirection: 'row',
    alignItems: 'center'
  },
  testItem: {
    marginHorizontal: 8,
    paddingVertical: 2
  }
})
