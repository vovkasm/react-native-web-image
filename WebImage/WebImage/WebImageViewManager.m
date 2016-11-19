#import "WebImageViewManager.h"

#import "RCTViewManager.h"

@interface WebImageViewManager : RCTViewManager

@end

@implementation WebImageViewManager

RCT_EXPORT_MODULE()

- (UIView*)view {
    return [[UIImageView alloc] init];
}

@end
