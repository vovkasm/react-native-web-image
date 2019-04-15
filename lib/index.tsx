import React from 'react'
import { Image, NativeSyntheticEvent, requireNativeComponent, ViewProperties } from 'react-native'

export type WebImageSource = {uri: string} | number

export interface IImageLoadEventData {
  source: {
    height: number,
    uri: string,
    width: number,
  }
}
export interface IImageErrorEventData {
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
   * Callback called on error.
   */
  onError?: (e: NativeSyntheticEvent<IImageErrorEventData>) => void

  /**
   * Callback called on success.
   */
  onLoad?: (e: NativeSyntheticEvent<IImageLoadEventData>) => void
}

interface IResolvedSource {
  uri: string
}
interface INativeProps extends ViewProperties {
  source: IResolvedSource
  resizeMode?: 'cover'|'contain'|'stretch'|'center'
  onWebImageError?: (e: NativeSyntheticEvent<IImageErrorEventData>) => void
  onWebImageLoad?: (e: NativeSyntheticEvent<IImageLoadEventData>) => void
}
const NativeWebImage = requireNativeComponent('WebImageView') as React.ComponentType<INativeProps>

const OMIT_PROPS_FOR_NATIVE: { [x: string]: boolean } = {
  children: true,
  onError: true,
  onLoad: true,
  source: true,
}
function omitPropsForNative(props: any): any {
  const native: any = {}
  const keys = Object.keys(props)
  for (const key of keys) {
    if (OMIT_PROPS_FOR_NATIVE[key]) continue
    native[key] = props[key]
  }
  return native
}

class WebImage extends React.Component<IWebImageProps> {
  static defaultProps = {
    resizeMode: 'contain',
  }

  render(): React.ReactNode {
    const nativeProps: INativeProps = omitPropsForNative(this.props)
    nativeProps.source = Image.resolveAssetSource(this.props.source)

    if (this.props.onLoad) nativeProps.onWebImageLoad = this._onLoad
    if (this.props.onError) nativeProps.onWebImageError = this._onError

    return React.createElement(NativeWebImage, nativeProps)
  }

  private _onLoad = (e: NativeSyntheticEvent<IImageLoadEventData>) => {
    if (this.props.onLoad) this.props.onLoad(e)
  }

  private _onError = (e: NativeSyntheticEvent<IImageErrorEventData>) => {
    if (this.props.onError) this.props.onError(e)
  }
}

export default WebImage
