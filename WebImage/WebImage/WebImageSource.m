#import "WebImageSource.h"

@implementation WebImageSource

- (instancetype)initWithURI:(NSString*)uri {
    self = [super init];
    if (self) {
        _uri = [uri copy];
    }
    return self;
}

- (instancetype)initWithDictionary:(NSDictionary*)json {
    self = [super init];
    if (self) {
        _uri = [json[@"uri"] copy];
        _resizeMode = [json[@"resizeMode"] copy];
    }
    return self;
}

@end
