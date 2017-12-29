#import "WebImageView.h"

#import <SDWebImage/UIImageView+WebCache.h>

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
        if (self.onWebImageLoad) {
            NSDictionary *dict = @{
                                   @"width": @(image.size.width),
                                   @"height": @(image.size.height),
                                   @"uri": imageURL.absoluteString,
                                   };
            NSDictionary* event = @{@"source": dict};
            self.onWebImageLoad(event);
        }
    }];
}

@end
