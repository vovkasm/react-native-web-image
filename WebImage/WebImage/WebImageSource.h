#import <Foundation/Foundation.h>

@interface WebImageSource : NSObject

@property (nonatomic) NSString* uri;

- (instancetype)initWithURI:(NSString*)uri;

@end
