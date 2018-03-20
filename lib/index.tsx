import PropTypes from 'prop-types'
import React from 'react'
import { requireNativeComponent, View, ViewProperties } from 'react-native'
import resolveAssetSource from 'react-native/Libraries/Image/resolveAssetSource'

const URISourcePropType = PropTypes.shape({
  uri: PropTypes.string,
})
const SourcePropType = PropTypes.oneOfType([URISourcePropType, PropTypes.number])

export type WebImageSource = {uri: string} | number

export interface IImageLoadEvent {
  nativeEvent: {
    uri: string,
    width: number,
    height: number,
  }
}

export interface IImageErrorEvent {
  error: string
  uri: string
}

export interface IWebImageProps extends ViewProperties {
  source: WebImageSource
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
  resizeMode?: 'cover'|'contain'|'stretch'|'center'

  /**
   * Callback called on error. With event object with props:
   */
  onError?: (e: IImageErrorEvent) => void

  /**
   * Callback called on success. With event object with props:
   */
  onLoad?: (event: IImageLoadEvent) => void
}

interface IResolvedSource {
  uri: string
}
interface INativeProps extends ViewProperties {
  source: IResolvedSource
  resizeMode?: 'cover'|'contain'|'stretch'|'center'
  onWebImageError?: (e: IImageErrorEvent) => void
  onWebImageLoad?: (e: IImageLoadEvent) => void
}
const NativeWebImage = requireNativeComponent<INativeProps>('WebImageView')

class WebImage extends React.Component<IWebImageProps> {
  static propTypes = {
    ...View.propTypes,
    resizeMode: PropTypes.oneOf(['cover', 'contain', 'stretch', 'center']),
    source: SourcePropType.isRequired,

    onError: PropTypes.func,
    onLoad: PropTypes.func,
  }

  static defaultProps = {
    resizeMode: 'contain',
  }

  render () {
    const { source, ...props } = this.props
    const resolvedSource = resolveAssetSource(source)

    return <NativeWebImage
      {...props}
      source={resolvedSource}
      onWebImageError={this._onError}
      onWebImageLoad={this._onLoad}
    />
  }

  _onLoad = (e: IImageLoadEvent) => {
    if (this.props.onLoad) this.props.onLoad(e)
  }

  _onError = (e: IImageErrorEvent) => {
    if (this.props.onError) this.props.onError(e)
  }
}

export default WebImage
