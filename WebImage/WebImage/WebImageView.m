#import "WebImageView.h"

@interface WebImageView ()

@property (nonatomic) BOOL needsImageUpdate;

@end

@implementation WebImageView

- (void)setSource:(WebImageSource *)source {
    _source = source;
    self.needsImageUpdate = YES;
}

- (void)didSetProps:(NSArray<NSString*>*)changedProps {
    if (!self.needsImageUpdate) return;
    self.needsImageUpdate = NO;
    
    typeof(self) __weak wSelf = self;
    [self sd_setImageWithURL:self.source.uri completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
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
    }];
}

@end
