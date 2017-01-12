#import "WebImageViewManager.h"
#import "WebImageSource.h"

#import <SDWebImage/UIImageView+WebCache.h>

#import <React/RCTViewManager.h>

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
    UIImageView* view = [[UIImageView alloc] init];
    view.contentMode = UIViewContentModeScaleAspectFit;
    view.clipsToBounds = YES;
    return view;
}

RCT_CUSTOM_VIEW_PROPERTY(source, WebImageSource, UIImageView) {
    if (json) {
        WebImageSource* src = [RCTConvert WebImageSource:json];
        [view sd_setImageWithURL:src.uri];
    }
}

RCT_CUSTOM_VIEW_PROPERTY(resizeMode, NSString, UIImageView) {
    if (json) {
        if ([@"cover" isEqualToString:json]) {
            view.contentMode = UIViewContentModeScaleAspectFill;
        } else if ([@"contain" isEqualToString:json]) {
            view.contentMode = UIViewContentModeScaleAspectFit;
        } else if ([@"stretch" isEqualToString:json]) {
            view.contentMode = UIViewContentModeScaleToFill;
        } else if ([@"center" isEqualToString:json]) {
            view.contentMode = UIViewContentModeCenter;
        }
    }
}

@end
