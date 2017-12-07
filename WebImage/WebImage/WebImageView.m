#import "WebImageView.h"

@implementation WebImageView

- (void)setSource:(WebImageSource *)source {
    _source = source;
    
    typeof(self) __weak wSelf = self;
    [self sd_setImageWithURL:source.uri completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
        dispatch_async(dispatch_get_main_queue(), ^{
            // Perform on next cycle, otherwise onWebImageSuccess is nil :-/
            typeof(self) self = wSelf;
            if (self == nil) return;
            
            if (error) {
                if (self.onWebImageError) {
                    self.onWebImageError(@{@"error":error.description, @"uri":imageURL.absoluteString});
                }
                return;
            }
            if (self.onWebImageSuccess) {
                NSMutableDictionary* event = [NSMutableDictionary dictionary];
                event[@"uri"] = imageURL.absoluteString;
                if (cacheType != SDImageCacheTypeNone) {
                    event[@"type"] = @"cache";
                }
                self.onWebImageSuccess(event);
            }
        });
    }];
}

@end
