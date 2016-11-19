import React, { PropTypes } from 'react'
import { requireNativeComponent, View } from 'react-native'

const resolveAssetSource = require('react-native/Libraries/Image/resolveAssetSource')

const URISourcePropType = PropTypes.shape({
  uri: PropTypes.string,
})
const SourcePropType = PropTypes.oneOfType([URISourcePropType, PropTypes.number]);

class WebImage extends React.Component {
  static propTypes = {
    ...View.propTypes,
    source: SourcePropType.isRequired,
  }
  render() {
    const { source, ...props } = this.props
    const resolvedSource = resolveAssetSource(source)

    return <WebImageView {...props} source={resolvedSource}/>
  }
}

var WebImageView = requireNativeComponent('WebImageView', WebImage)

export default WebImage
