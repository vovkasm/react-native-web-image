#import "WebImageSource.h"

@implementation WebImageSource

- (instancetype)initWithURI:(NSString*)uri {
    self = [super init];
    if (self) {
        _uri = [uri copy];
    }
    return self;
}

@end
