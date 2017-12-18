#import <XCTest/XCTest.h>

#import <WebImage/WebImageView.h>

@interface WebImageViewSpec : XCTestCase
@end

@implementation WebImageViewSpec

- (void)testShouldLoadImage {
    XCTestExpectation* expectLoad = [[XCTestExpectation alloc] initWithDescription:@"onLoad called"];
    expectLoad.inverted = YES;
    XCTestExpectation* expectError = [[XCTestExpectation alloc] initWithDescription:@"onError called"];
    expectError.expectedFulfillmentCount = 1;
    expectError.assertForOverFulfill = YES;
    
    WebImageView* imageView = [[WebImageView alloc] initWithFrame:CGRectMake(0, 0, 100, 100)];
    imageView.onWebImageLoad = ^(NSDictionary *body) {
        [expectLoad fulfill];
    };
    imageView.onWebImageError = ^(NSDictionary *body) {
        [expectError fulfill];
    };
    imageView.source = [[WebImageSource alloc] initWithURIString:@"http://fake/favicon.png"];
    [imageView didSetProps:@[@"onWebImageError", @"onWebImageLoad", @"source"]];
    
    [self waitForExpectations:@[expectLoad, expectError] timeout:2.0];
}

@end
