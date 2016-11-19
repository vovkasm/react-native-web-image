#import "WebImageViewManager.h"
#import "WebImageSource.h"

#import <SDWebImage/UIImageView+WebCache.h>

#import "RCTViewManager.h"

@interface WebImageViewManager : RCTViewManager

@end

@implementation RCTConvert (WebImageSource)

+ (WebImageSource*)WebImageSource:(id)json {
    json = [self NSDictionary:json];
    return [[WebImageSource alloc] initWithURI:json[@"uri"]];
}

@end

@implementation WebImageViewManager

RCT_EXPORT_MODULE()

- (UIView*)view {
    return [[UIImageView alloc] init];
}

RCT_CUSTOM_VIEW_PROPERTY(source, WebImageSource, UIImageView) {
    if (json) {
        WebImageSource* src = [RCTConvert WebImageSource:json];
        [view sd_setImageWithURL:src.uri];
    }
}

@end
