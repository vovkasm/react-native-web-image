#import "WebImageViewManager.h"
#import "WebImageSource.h"
#import "WebImageView.h"

#import <React/RCTViewManager.h>

@interface WebImageViewManager : RCTViewManager

@end

@implementation RCTConvert (WebImageSource)

+ (WebImageSource*)WebImageSource:(id)json {

    json = [self NSDictionary:json];
    return [[WebImageSource alloc] initWithURIString:json[@"uri"]];
}

@end

@implementation WebImageViewManager

RCT_EXPORT_MODULE()

- (UIView*)view {
    WebImageView* view = [[WebImageView alloc] init];
    view.contentMode = UIViewContentModeScaleAspectFit;
    view.clipsToBounds = YES;
    return view;
}

RCT_CUSTOM_VIEW_PROPERTY(source, WebImageSource, WebImageView) {
    if (json) {
        view.source = [RCTConvert WebImageSource:json];
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

RCT_EXPORT_VIEW_PROPERTY(onWebImageError, RCTDirectEventBlock);

@end
