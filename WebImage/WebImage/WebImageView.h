#import <UIKit/UIKit.h>

#import <SDWebImage/UIImageView+WebCache.h>

#import <React/RCTComponent.h>

#import "WebImageSource.h"

@interface WebImageView : UIImageView

@property (nonatomic, copy) RCTDirectEventBlock onError;
@property (nonatomic) WebImageSource* source;

@end
