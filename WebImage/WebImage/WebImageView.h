#import <UIKit/UIKit.h>

#import <React/RCTComponent.h>

#import "WebImageSource.h"

@interface WebImageView : UIImageView

@property (nonatomic, copy) RCTDirectEventBlock onWebImageError;
@property (nonatomic, copy) RCTDirectEventBlock onWebImageLoad;
@property (nonatomic) WebImageSource* source;

- (void)didSetProps:(NSArray<NSString*>*)changedProps;

@end
