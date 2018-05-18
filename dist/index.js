"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const tslib_1 = require("tslib");
const prop_types_1 = tslib_1.__importDefault(require("prop-types"));
const react_1 = tslib_1.__importDefault(require("react"));
const react_native_1 = require("react-native");
const resolveAssetSource_1 = tslib_1.__importDefault(require("react-native/Libraries/Image/resolveAssetSource"));
const URISourcePropType = prop_types_1.default.shape({
    uri: prop_types_1.default.string,
});
const SourcePropType = prop_types_1.default.oneOfType([URISourcePropType, prop_types_1.default.number]);
const NativeWebImage = react_native_1.requireNativeComponent('WebImageView');
const OMIT_PROPS_FOR_NATIVE = {
    children: true,
    onError: true,
    onLoad: true,
    source: true,
};
function omitPropsForNative(props) {
    const native = {};
    const keys = Object.keys(props);
    for (const key of keys) {
        if (OMIT_PROPS_FOR_NATIVE[key])
            continue;
        native[key] = props[key];
    }
    return native;
}
class WebImage extends react_1.default.Component {
    constructor() {
        super(...arguments);
        this._onLoad = (e) => {
            if (this.props.onLoad)
                this.props.onLoad(e);
        };
        this._onError = (e) => {
            if (this.props.onError)
                this.props.onError(e);
        };
    }
    render() {
        const nativeProps = omitPropsForNative(this.props);
        nativeProps.source = resolveAssetSource_1.default(this.props.source);
        if (this.props.onLoad)
            nativeProps.onWebImageLoad = this._onLoad;
        if (this.props.onError)
            nativeProps.onWebImageError = this._onError;
        return react_1.default.createElement(NativeWebImage, nativeProps);
    }
}
WebImage.propTypes = Object.assign({}, react_native_1.ViewPropTypes, { resizeMode: prop_types_1.default.oneOf(['cover', 'contain', 'stretch', 'center']), source: SourcePropType.isRequired, onError: prop_types_1.default.func, onLoad: prop_types_1.default.func });
WebImage.defaultProps = {
    resizeMode: 'contain',
};
exports.default = WebImage;
