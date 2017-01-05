#import <Foundation/Foundation.h>

@interface WebImageSource : NSObject

@property (nonatomic) NSString* uri;
@property (nonatomic) NSString* resizeMode;

- (instancetype)initWithURI:(NSString*)uri;
- (instancetype)initWithDictionary:(NSDictionary*)json;

@end
