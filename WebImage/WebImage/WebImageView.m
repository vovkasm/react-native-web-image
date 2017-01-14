#import "WebImageView.h"

@implementation WebImageView

- (void)setSource:(WebImageSource *)source {
    _source = source;
    [self sd_setImageWithURL:source.uri completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
        if (error) {
            if (_onError) {
                _onError(@{@"error":error.description, @"uri":imageURL.absoluteString});
            }
            return;
        }
    }];
}

@end
