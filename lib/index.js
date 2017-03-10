'use strict'

import React, { PropTypes } from 'react'
import { requireNativeComponent, View } from 'react-native'

const resolveAssetSource = require('react-native/Libraries/Image/resolveAssetSource')

const URISourcePropType = PropTypes.shape({
  uri: PropTypes.string
})
const SourcePropType = PropTypes.oneOfType([URISourcePropType, PropTypes.number])

class WebImage extends React.Component {
  static propTypes = {
    ...View.propTypes,
    source: SourcePropType.isRequired,

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
     * 'stretch': Scale width and height independently, This may change the
     * aspect ratio of the src.
     *
     * 'center': Scale the image down so that it is completely visible,
     * if bigger than the area of the view.
     * The image will not be scaled up.
     */
    resizeMode: PropTypes.oneOf(['cover', 'contain', 'stretch', 'center']),

    /**
     * Callback called on error. With event.nativeEvent object with props:
     *   error - error message
     *   uri - URL that result this error
     */
    onError: PropTypes.func
  }

  render () {
    const { source, ...props } = this.props
    const resolvedSource = resolveAssetSource(source)

    return <WebImageView {...props} source={resolvedSource} onWebImageError={this._onError} />
  }

  _onError = (e) => {
    this.props.onError && this.props.onError(e.nativeEvent)
  }
}

WebImage.defaultProps = {
  resizeMode: 'contain'
}

var WebImageView = requireNativeComponent('WebImageView', WebImage, {
  nativeOnly: {onWebImageError: true}
})

export default WebImage
