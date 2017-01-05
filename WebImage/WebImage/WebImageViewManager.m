#import "WebImageViewManager.h"
#import "WebImageSource.h"

#import <SDWebImage/UIImageView+WebCache.h>

#import "RCTViewManager.h"

@interface WebImageViewManager : RCTViewManager

@end

@implementation RCTConvert (WebImageSource)

+ (WebImageSource*)WebImageSource:(id)json {
    json = [self NSDictionary:json];
    return [[WebImageSource alloc] initWithDictionary:json];
}

@end

@implementation WebImageViewManager

RCT_EXPORT_MODULE()

- (UIView*)view {
    UIImageView* view = [[UIImageView alloc] init];
    view.contentMode = UIViewContentModeScaleAspectFit;
    return view;
}

RCT_CUSTOM_VIEW_PROPERTY(source, WebImageSource, UIImageView) {
    if (json) {
        WebImageSource* src = [RCTConvert WebImageSource:json];
        
        if ([@"cover" isEqualToString:src.resizeMode]) {
            view.contentMode = UIViewContentModeScaleAspectFill;
        } else if ([@"contain" isEqualToString:src.resizeMode]) {
            view.contentMode = UIViewContentModeScaleAspectFit;
        }

        [view sd_setImageWithURL:src.uri];
    }
}

@end
