import React from 'react'
import { ActivityIndicator, FlatList, StyleSheet, View } from 'react-native'

import WebImage from 'react-native-web-image'

const s = StyleSheet.create({
  item: {
    borderColor: 'rgb(0,0,0)',
    borderWidth: StyleSheet.hairlineWidth,
    height: 120,
    paddingVertical: 2,
  },
  itemImage: {
    flex: 1,
  },
  list: {
    backgroundColor: 'white',
    flex: 1,
  },
})

interface IBrowserImageItem {
  id: string
  uri: string
}

interface IItemProps {
  item: IBrowserImageItem
}
class Item extends React.PureComponent<IItemProps> {
  render() {
    const uri = this.props.item.uri
    return (
      <View style={s.item}>
        <WebImage style={s.itemImage} resizeMode="cover" source={{ uri }} onError={this._onError} />
      </View>
    )
  }
  _onError = (e) => {
    // tslint:disable-next-line: no-console
    console.log(e)
  }
}

interface IBrowserProps {
  dataSource: () => Promise<IBrowserImageItem[]>
}
interface IBrowserState {
  loading: boolean
  items: IBrowserImageItem[] | undefined
}

// tslint:disable-next-line: max-classes-per-file
class Browser extends React.Component<IBrowserProps, IBrowserState> {
  constructor(props: IBrowserProps, ctx: any) {
    super(props, ctx)

    this.state = { loading: true, items: undefined }
  }

  componentDidMount() {
    this.props.dataSource().then((items) => {
      this.setState({ loading: false, items })
    })
  }

  render() {
    if (this.state.loading) {
      return <ActivityIndicator />
    }
    return (
      <FlatList
        style={s.list}
        data={this.state.items}
        renderItem={this._renderItem}
        keyExtractor={this._keyExtractor}
        initialNumToRender={10} // this can save some memory... why?
      />
    )
  }
  _renderItem = ({ item }) => {
    return <Item item={item} />
  }
  _keyExtractor = (item, index) => {
    return item.id
  }
}

export default Browser
