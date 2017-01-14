#import <Foundation/Foundation.h>

@interface WebImageSource : NSObject

@property (nonatomic) NSURL* uri;

- (instancetype)initWithURIString:(NSString*)uri;

@end
