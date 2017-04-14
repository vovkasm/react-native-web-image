import React, { PropTypes } from 'react'
import { Button, ScrollView, StyleSheet, Text, View } from 'react-native'

import Samples from './samples'
import TestDefault from './test-default'
import TestCrash from './test-activity-destroy-crash'
import TestBordersRectangleMono from './test-borders-rectangle-mono'
import TestBordersRectangleColor from './test-borders-rectangle-color'
import TestBordersRoundMono from './test-borders-round-mono'
import TestBordersRoundColor from './test-borders-round-color'
import BenchNoDecor from './bench-no-decor'

const TestCaseLink = ({ title, onPress }) => {
  return <View style={s.testItem}><Button title={title} onPress={onPress} /></View>
}
TestCaseLink.propTypes = {
  onPress: PropTypes.func.isRequired,
  title: PropTypes.string.isRequired
}

const Main = ({ navigateTo }) => {
  return <ScrollView>
    <TestCaseLink title='Samples' onPress={() => { navigateTo('/samples') }} />
    <TestCaseLink title='Default params' onPress={() => { navigateTo('/test-default') }} />
    <TestCaseLink title='Borders Rectangle Mono' onPress={() => { navigateTo('/test-borders/rectangle/mono') }} />
    <TestCaseLink title='Borders Rectangle Color' onPress={() => { navigateTo('/test-borders/rectangle/color') }} />
    <TestCaseLink title='Borders Round Mono' onPress={() => { navigateTo('/test-borders/round/mono') }} />
    <TestCaseLink title='Borders Round Color' onPress={() => { navigateTo('/test-borders/round/color') }} />
    <TestCaseLink title='Bench (no decorations)' onPress={() => { navigateTo('/bench/no-decor') }} />
    <TestCaseLink title='Crashes' onPress={() => { navigateTo('/test-crash') }} />
  </ScrollView>
}
Main.propTypes = {
  navigateTo: PropTypes.func.isRequired
}

const routes = {
  '/': Main,
  '/samples': Samples,
  '/test-default': TestDefault,
  '/test-borders/rectangle/mono': TestBordersRectangleMono,
  '/test-borders/rectangle/color': TestBordersRectangleColor,
  '/test-borders/round/mono': TestBordersRoundMono,
  '/test-borders/round/color': TestBordersRoundColor,
  '/bench/no-decor': BenchNoDecor,
  '/test-crash': TestCrash
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
