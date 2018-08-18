import React from 'react';
import { NativeSyntheticEvent, ViewProperties } from 'react-native';
export declare type WebImageSource = {
    uri: string;
} | number;
export interface IImageLoadEventData {
    source: {
        height: number;
        uri: string;
        width: number;
    };
}
export interface IImageErrorEventData {
    error: string;
    uri: string;
}
export interface IWebImageProps extends ViewProperties {
    source: WebImageSource;
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
    resizeMode?: 'cover' | 'contain' | 'stretch' | 'center';
    /**
     * Callback called on error.
     */
    onError?: (e: NativeSyntheticEvent<IImageErrorEventData>) => void;
    /**
     * Callback called on success.
     */
    onLoad?: (e: NativeSyntheticEvent<IImageLoadEventData>) => void;
}
declare class WebImage extends React.Component<IWebImageProps> {
    static defaultProps: {
        resizeMode: string;
    };
    render(): React.ReactNode;
    private _onLoad;
    private _onError;
}
export default WebImage;
