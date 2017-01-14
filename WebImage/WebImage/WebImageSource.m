#import "WebImageSource.h"

@implementation WebImageSource

- (instancetype)initWithURIString:(NSString*)uri {
    self = [super init];
    if (self) {
        _uri = [NSURL URLWithString:[uri copy]];
    }
    return self;
}

@end
