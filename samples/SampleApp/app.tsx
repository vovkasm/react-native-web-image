import React from 'react'
import { Button, ScrollView, StyleSheet, Text, View } from 'react-native'

import BenchNoDecor from './bench-no-decor'
import CallbackSamples from './callback-samples'
import Browser from './image-browser'
import loadBigImages from './loadBigImages'
import loadNasaImages from './loadNasaImages'
import Samples from './samples'
import TestCrash from './test-activity-destroy-crash'
import TestBordersRectangleColor from './test-borders-rectangle-color'
import TestBordersRectangleMono from './test-borders-rectangle-mono'
import TestBordersRoundColor from './test-borders-round-color'
import TestBordersRoundMono from './test-borders-round-mono'
import TestDefault from './test-default'

const TestCaseLink: React.FC<{ title: string; onPress?: () => void }> = ({ title, onPress }) => {
  return (
    <View style={s.testItem}>
      <Button title={title} onPress={onPress} />
    </View>
  )
}

const Main: React.FC<{ navigateTo: (to: string) => void }> = ({ navigateTo }) => {
  return (
    <ScrollView>
      <TestCaseLink
        title="Browse Earth"
        onPress={() => {
          navigateTo('/browser/nasa')
        }}
      />
      <TestCaseLink
        title="Browse big images"
        onPress={() => {
          navigateTo('/browser/big')
        }}
      />
      <TestCaseLink
        title="Samples"
        onPress={() => {
          navigateTo('/samples/sizing')
        }}
      />
      <TestCaseLink
        title="Callbacks"
        onPress={() => {
          navigateTo('/samples/callbacks')
        }}
      />
      <TestCaseLink
        title="Default params"
        onPress={() => {
          navigateTo('/test-default')
        }}
      />
      <TestCaseLink
        title="Borders Rectangle Mono"
        onPress={() => {
          navigateTo('/test-borders/rectangle/mono')
        }}
      />
      <TestCaseLink
        title="Borders Rectangle Color"
        onPress={() => {
          navigateTo('/test-borders/rectangle/color')
        }}
      />
      <TestCaseLink
        title="Borders Round Mono"
        onPress={() => {
          navigateTo('/test-borders/round/mono')
        }}
      />
      <TestCaseLink
        title="Borders Round Color"
        onPress={() => {
          navigateTo('/test-borders/round/color')
        }}
      />
      <TestCaseLink
        title="Bench (no decorations)"
        onPress={() => {
          navigateTo('/bench/no-decor')
        }}
      />
      <TestCaseLink
        title="Crashes"
        onPress={() => {
          navigateTo('/test-crash')
        }}
      />
    </ScrollView>
  )
}

const routes = {
  '/': Main,
  '/bench/no-decor': BenchNoDecor,
  '/browser/big': () => <Browser dataSource={loadBigImages} />,
  '/browser/nasa': () => <Browser dataSource={loadNasaImages} />,
  '/samples/callbacks': CallbackSamples,
  '/samples/sizing': Samples,
  '/test-borders/rectangle/color': TestBordersRectangleColor,
  '/test-borders/rectangle/mono': TestBordersRectangleMono,
  '/test-borders/round/color': TestBordersRoundColor,
  '/test-borders/round/mono': TestBordersRoundMono,
  '/test-crash': TestCrash,
  '/test-default': TestDefault,
}

interface IAppState {
  route: keyof typeof routes
}

export default class App extends React.Component<{}, IAppState> {
  constructor(props: any, ctx: any) {
    super(props, ctx)
    this.state = {
      route: '/',
    }
  }
  navigateTo = (route) => {
    this.setState({ route })
  }
  render() {
    const route = this.state.route
    const comp = routes[route]
    if (comp === undefined) {
      throw new Error(`Component for '${route}' not found`)
    }
    return (
      <View style={s.screen}>
        {this.renderHeaderFor(comp)}
        <View style={s.scene}>{this.renderSceneComp(comp)}</View>
      </View>
    )
  }
  renderSceneComp(comp) {
    return React.createElement(comp, { navigateTo: this.navigateTo }, null)
  }
  renderHeaderFor(comp) {
    const title = comp.title || comp.name || comp.className
    return (
      <View style={s.header}>
        <Button
          title="Back"
          onPress={() => {
            this.navigateTo('/')
          }}
        />
        <Text>{title}</Text>
      </View>
    )
  }
}

const s = StyleSheet.create({
  header: {
    alignItems: 'center',
    flexDirection: 'row',
    marginBottom: 2,
    marginHorizontal: 8,
    marginTop: 15,
  },
  scene: {
    flex: 1,
  },
  screen: {
    flex: 1,
  },
  testItem: {
    marginHorizontal: 8,
    paddingVertical: 2,
  },
})
