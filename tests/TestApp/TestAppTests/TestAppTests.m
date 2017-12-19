#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>

#import <SDWebImage/SDWebImageManager.h>
#import <WebImage/WebImageView.h>

@interface TestSDWebImageManager : SDWebImageManager
@end

@interface TestSDWebImageOperation : NSObject <SDWebImageOperation>
@property (nonatomic, getter=isCancelled) BOOL cancelled;
@end

@interface WebImageViewSpec : XCTestCase
@end

@implementation WebImageViewSpec

- (void)testShouldLoadImage {
    TestSDWebImageManager* sdMan = [[TestSDWebImageManager alloc] init];
    id sdMock = OCMClassMock([SDWebImageManager class]);
    OCMStub([sdMock sharedManager]).andReturn(sdMan);
    
    XCTestExpectation* expectLoad = [[XCTestExpectation alloc] initWithDescription:@"onLoad called"];
    expectLoad.expectedFulfillmentCount = 1;
    expectLoad.assertForOverFulfill = YES;
    
    WebImageView* imageView = [[WebImageView alloc] initWithFrame:CGRectMake(0, 0, 100, 100)];
    imageView.onWebImageLoad = ^(NSDictionary *body) {
        NSDictionary* expected = @{
                                   @"source": @{
                                           @"uri": @"http://fake/favicon.png",
                                           @"width": @(64),
                                           @"height": @(64),
                                           },
                                   };
        XCTAssertEqualObjects(body, expected);
        [expectLoad fulfill];
    };
    imageView.onWebImageError = ^(NSDictionary *body) {
        XCTFail(@"on error should not be called");
    };
    imageView.source = [[WebImageSource alloc] initWithURIString:@"http://fake/favicon.png"];
    [imageView didSetProps:@[@"onWebImageError", @"onWebImageLoad", @"source"]];
    
    [self waitForExpectations:@[expectLoad] timeout:2.0];
}

@end

@implementation TestSDWebImageManager

- (nullable id <SDWebImageOperation>)loadImageWithURL:(nullable NSURL *)url options:(SDWebImageOptions)options progress:(nullable SDWebImageDownloaderProgressBlock)progressBlock
 completed:(nullable SDInternalCompletionBlock)completedBlock {
    NSString* sampleBase64 = @"iVBORw0KGgoAAAANSUhEUgAAAEAAAABAAgMAAADXB5lNAAAACVBMVEUAzAD///9mZmaF427XAAAAMElEQVQ4y2NYFYoKGcgQQOOvIkdgMLmDAQrgtpAoADYICuC2kCgw6o7B647R/AKHAAPjfpCz9iukAAAAAElFTkSuQmCC";
    NSData* data = [[NSData alloc] initWithBase64EncodedString:sampleBase64 options:0];
    UIImage* image = [UIImage imageWithData:data];
    dispatch_async(dispatch_get_main_queue(), ^{
        completedBlock(image, data, nil, SDImageCacheTypeNone, YES, url);
    });
    return [[TestSDWebImageOperation alloc] init];
}

@end

@implementation TestSDWebImageOperation

- (void)cancel {
    self.cancelled = YES;
}

@end

