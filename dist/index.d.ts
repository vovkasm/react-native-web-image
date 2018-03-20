/// <reference types="react" />
/// <reference types="react-native" />
import PropTypes from 'prop-types';
import React from 'react';
import { NativeSyntheticEvent, ViewProperties } from 'react-native';
export declare type WebImageSource = {
    uri: string;
} | number;
export interface IImageLoadEventData {
    uri: string;
    width: number;
    height: number;
}
export interface IImageErrorEventData {
    error: string;
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
    static propTypes: {
        resizeMode: PropTypes.Requireable<any>;
        source: PropTypes.Validator<any>;
        onError: PropTypes.Requireable<any>;
        onLoad: PropTypes.Requireable<any>;
    } | {
        resizeMode: PropTypes.Requireable<any>;
        source: PropTypes.Validator<any>;
        onError: PropTypes.Requireable<any>;
        onLoad: PropTypes.Requireable<any>;
        hitSlop?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        onLayout?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        pointerEvents?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        removeClippedSubviews?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        style?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        testID?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        collapsable?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        needsOffscreenAlphaCompositing?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        renderToHardwareTextureAndroid?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        accessibilityViewIsModal?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        accessibilityActions?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        onAccessibilityAction?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        shouldRasterizeIOS?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        onStartShouldSetResponder?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        onMoveShouldSetResponder?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        onResponderEnd?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        onResponderGrant?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        onResponderReject?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        onResponderMove?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        onResponderRelease?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        onResponderStart?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        onResponderTerminationRequest?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        onResponderTerminate?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        onStartShouldSetResponderCapture?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        onMoveShouldSetResponderCapture?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        onTouchStart?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        onTouchMove?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        onTouchEnd?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        onTouchCancel?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        onTouchEndCapture?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        accessible?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        accessibilityLabel?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        accessibilityComponentType?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        accessibilityLiveRegion?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        importantForAccessibility?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        accessibilityTraits?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        onAcccessibilityTap?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
        onMagicTap?: ((object: ViewProperties, key: string, componentName: string, ...rest: any[]) => Error | null) | undefined;
    };
    static defaultProps: {
        resizeMode: string;
    };
    render(): React.ReactNode;
    private _onLoad;
    private _onError;
}
export default WebImage;
