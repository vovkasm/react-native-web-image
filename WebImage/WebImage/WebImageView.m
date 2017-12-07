#import "WebImageView.h"

@implementation WebImageView{
    BOOL isCallback;
}

- (void)setSource:(WebImageSource *)source {
    _source = source;
     isCallback= NO;
    
    SDWebImageManager* manager =[[SDWebImageManager alloc] init];
    [manager cachedImageExistsForURL:source.uri completion:^(BOOL inCache) {
        if (!inCache || isCallback) return;
        if (_onWebImageSuccess) {
            _onWebImageSuccess(@{@"uri":self->_source.uri.absoluteString, @"type": @"cache"});
        }
        
        isCallback = YES;
    }];
    
    [self sd_setImageWithURL:source.uri completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
        if (error) {
            if (_onWebImageError) {
                _onWebImageError(@{@"error":error.description, @"uri":imageURL.absoluteString});
            }
            return;
        }
        else {
            if (_onWebImageSuccess && !self->isCallback) {
                self->isCallback = YES;
                _onWebImageSuccess(@{@"uri":imageURL.absoluteString});
            }
        }
    }];
}

@end
