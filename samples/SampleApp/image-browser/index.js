import PropTypes from 'prop-types'
import React from 'react'
import { ActivityIndicator, FlatList, StyleSheet, View } from 'react-native'
import WebImage from 'react-native-web-image'

const s = StyleSheet.create({
  item: {
    paddingVertical: 2,
    borderWidth: StyleSheet.hairlineWidth,
    borderColor: 'rgb(0,0,0)',
    height: 120,
  },
  itemImage: {
    flex: 1,
  },
  list: {
    flex: 1,
    backgroundColor: 'white',
  }
})

class Item extends React.PureComponent {
  static propTypes = {
    item: PropTypes.shape({
      id: PropTypes.string.isRequired,
      uri: PropTypes.string.isRequired,
    }).isRequired,
  }

  render () {
    const uri = this.props.item.uri
    return <View style={s.item}>
      <WebImage style={s.itemImage} resizeMode='cover' source={{uri}} onError={this._onError} />
    </View>
  }
  _onError = (e) => {
    console.log(e)
  }
}

class Browser extends React.Component {
  constructor (props, ctx) {
    super(props, ctx)

    this.state = {
      loading: true,
      items: undefined,
    }
  }
  componentDidMount () {
    loadNasaImages().then((items) => {
      this.setState({
        loading: false,
        items,
      })
    })
  }
  render () {
    if (this.state.loading) {
      return <ActivityIndicator />
    }
    return <FlatList
      style={s.list}
      data={this.state.items}
      renderItem={this._renderItem}
      keyExtractor={this._keyExtractor}
    />
  }
  _renderItem = ({ item }) => {
    return <Item item={item} />
  }
  _keyExtractor = (item, index) => {
    return item.id
  }
}

function loadNasaImages () {
  return fetch('https://images-api.nasa.gov/search?media_type=image&q=earth')
    .then((res) => {
      return res.json()
    })
    .then((data) => {
      if (!data || !data.collection || !data.collection.items) return []
      const dataItems = data.collection.items
      const items = []
      for (const dataItem of dataItems) {
        const item = {
          id: dataItem.data[0].nasa_id,
          title: dataItem.data[0].title,
          uri: dataItem.links[0].href,
        }
        items.push(item)
      }
      return items
    })
}

export default Browser
