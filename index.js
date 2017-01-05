import React, { PropTypes } from 'react'
import { requireNativeComponent, View } from 'react-native'

const resolveAssetSource = require('react-native/Libraries/Image/resolveAssetSource')

const URISourcePropType = PropTypes.shape({
  uri: PropTypes.string,

  /**
     * Determines how to resize the image when the frame doesn't match the raw
     * image dimensions.
     *
     * 'cover': Scale the image uniformly (maintain the image's aspect ratio)
     * so that both dimensions (width and height) of the image will be equal
     * to or larger than the corresponding dimension of the view (minus padding).
     *
     * 'contain': Scale the image uniformly (maintain the image's aspect ratio)
     * so that both dimensions (width and height) of the image will be equal to
     * or less than the corresponding dimension of the view (minus padding).
     *
     */
  resizeMode: PropTypes.oneOf(['cover', 'contain']),
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
