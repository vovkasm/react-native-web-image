#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>

#import <SDWebImage/SDWebImageManager.h>
#import <RNWebImage/WebImageView.h>

@interface TestSDWebImageManager : SDWebImageManager
@property (nonatomic) SDImageCacheType cacheType;
@property (nonatomic) NSError* error;
@end

@interface TestSDWebImageOperation : NSObject <SDWebImageOperation>
@property (nonatomic, getter=isCancelled) BOOL cancelled;
@end

@interface WebImageViewSpec : XCTestCase
@property (nonatomic) TestSDWebImageManager* sdManager;
@property (nonatomic) id sdManagerMock;
@end

@implementation WebImageViewSpec

- (void)setUp {
    self.sdManager = [[TestSDWebImageManager alloc] init];
    self.sdManagerMock = OCMClassMock([SDWebImageManager class]);
    OCMStub([self.sdManagerMock sharedManager]).andReturn(self.sdManager);
}

- (void)tearDown {
    [self.sdManagerMock stopMocking];
    self.sdManagerMock = nil;
    self.sdManager = nil;
}

- (void)testSimpleLoadNoCache {
    WebImageView* imageView = [[WebImageView alloc] initWithFrame:CGRectMake(0, 0, 100, 100)];
    XCTAssertNil(imageView.image);
    
    XCTKVOExpectation* imageSettled = [[XCTKVOExpectation alloc] initWithKeyPath:@"image" object:imageView];
    imageSettled.handler = ^BOOL(id  _Nonnull observedObject, NSDictionary * _Nonnull change) {
        WebImageView* imageView = observedObject;
        if ([change[NSKeyValueChangeKindKey] unsignedIntegerValue] == NSKeyValueChangeSetting) {
            if (imageView.image != nil) {
                XCTAssertEqualWithAccuracy(imageView.image.size.width, 64, 0.001);
                XCTAssertEqualWithAccuracy(imageView.image.size.height, 64, 0.001);
                return YES;
            }
        }
        return NO;
    };
    
    imageView.source = [[WebImageSource alloc] initWithURIString:@"http://fake/favicon.png"];
    [imageView didSetProps:@[@"source"]];

    [self waitForExpectations:@[imageSettled] timeout:2.0];
}

- (void)testSimpleLoadDiskCache {
    self.sdManager.cacheType = SDImageCacheTypeDisk;
    
    WebImageView* imageView = [[WebImageView alloc] initWithFrame:CGRectMake(0, 0, 100, 100)];
    XCTAssertNil(imageView.image);
    
    XCTKVOExpectation* imageSettled = [[XCTKVOExpectation alloc] initWithKeyPath:@"image" object:imageView];
    imageSettled.handler = ^BOOL(id  _Nonnull observedObject, NSDictionary * _Nonnull change) {
        WebImageView* imageView = observedObject;
        if ([change[NSKeyValueChangeKindKey] unsignedIntegerValue] == NSKeyValueChangeSetting) {
            if (imageView.image != nil) {
                XCTAssertEqualWithAccuracy(imageView.image.size.width, 64, 0.001);
                XCTAssertEqualWithAccuracy(imageView.image.size.height, 64, 0.001);
                return YES;
            }
        }
        return NO;
    };
    
    imageView.source = [[WebImageSource alloc] initWithURIString:@"http://fake/favicon.png"];
    [imageView didSetProps:@[@"source"]];
    
    [self waitForExpectations:@[imageSettled] timeout:2.0];
}

- (void)testSimpleLoadMemCache {
    self.sdManager.cacheType = SDImageCacheTypeMemory;
    
    WebImageView* imageView = [[WebImageView alloc] initWithFrame:CGRectMake(0, 0, 100, 100)];
    XCTAssertNil(imageView.image);
    
    XCTKVOExpectation* imageSettled = [[XCTKVOExpectation alloc] initWithKeyPath:@"image" object:imageView];
    imageSettled.handler = ^BOOL(id  _Nonnull observedObject, NSDictionary * _Nonnull change) {
        WebImageView* imageView = observedObject;
        if ([change[NSKeyValueChangeKindKey] unsignedIntegerValue] == NSKeyValueChangeSetting) {
            if (imageView.image != nil) {
                XCTAssertEqualWithAccuracy(imageView.image.size.width, 64, 0.001);
                XCTAssertEqualWithAccuracy(imageView.image.size.height, 64, 0.001);
                return YES;
            }
        }
        return NO;
    };
    
    imageView.source = [[WebImageSource alloc] initWithURIString:@"http://fake/favicon.png"];
    [imageView didSetProps:@[@"source"]];
    
    [self waitForExpectations:@[imageSettled] timeout:2.0];
}

- (void)testShouldCallOnLoad {
    XCTestExpectation* onLoadCalled = [[XCTestExpectation alloc] initWithDescription:@"onLoad called"];
    onLoadCalled.expectedFulfillmentCount = 1;
    onLoadCalled.assertForOverFulfill = YES;
    
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
        [onLoadCalled fulfill];
    };
    imageView.onWebImageError = ^(NSDictionary *body) {
        XCTFail(@"onError should not be called");
    };
    imageView.source = [[WebImageSource alloc] initWithURIString:@"http://fake/favicon.png"];
    [imageView didSetProps:@[@"onWebImageError", @"onWebImageLoad", @"source"]];
    
    [self waitForExpectations:@[onLoadCalled] timeout:2.0];
}

- (void)testShouldCallOnError {
    self.sdManager.error = [NSError errorWithDomain:@"test" code:1 userInfo:@{NSLocalizedDescriptionKey: @"test error"}];
    
    XCTestExpectation* onErrorCalled = [[XCTestExpectation alloc] initWithDescription:@"onError called"];
    onErrorCalled.expectedFulfillmentCount = 1;
    onErrorCalled.assertForOverFulfill = YES;
    
    WebImageView* imageView = [[WebImageView alloc] initWithFrame:CGRectMake(0, 0, 100, 100)];
    imageView.onWebImageLoad = ^(NSDictionary *body) {
        XCTFail(@"onLoad should not be called");
    };
    imageView.onWebImageError = ^(NSDictionary *body) {
        NSDictionary* expected = @{
                                   @"uri": @"http://fake/error.png",
                                   @"error": @"Error Domain=test Code=1 \"test error\" UserInfo={NSLocalizedDescription=test error}",
                                   };
        XCTAssertEqualObjects(body, expected);
        [onErrorCalled fulfill];
    };
    imageView.source = [[WebImageSource alloc] initWithURIString:@"http://fake/error.png"];
    [imageView didSetProps:@[@"onWebImageError", @"onWebImageLoad", @"source"]];
    
    [self waitForExpectations:@[onErrorCalled] timeout:2.0];
}

@end

@implementation TestSDWebImageManager

- (instancetype)init {
    self = [super init];
    if (self) {
        _cacheType = SDImageCacheTypeNone;
        _error = nil;
    }
    return self;
}

- (nullable SDWebImageCombinedOperation *)loadImageWithURL:(nullable NSURL *)url
                                                   options:(SDWebImageOptions)options
                                                   context:(nullable SDWebImageContext *)context
                                                  progress:(nullable SDImageLoaderProgressBlock)progressBlock
                                                 completed:(nonnull SDInternalCompletionBlock)completedBlock {
    NSString* sampleBase64 = @"iVBORw0KGgoAAAANSUhEUgAAAEAAAABAAgMAAADXB5lNAAAACVBMVEUAzAD///9mZmaF427XAAAAMElEQVQ4y2NYFYoKGcgQQOOvIkdgMLmDAQrgtpAoADYICuC2kCgw6o7B647R/AKHAAPjfpCz9iukAAAAAElFTkSuQmCC";
    NSData* data = [[NSData alloc] initWithBase64EncodedString:sampleBase64 options:0];
    UIImage* image = [UIImage imageWithData:data];
    SDImageCacheType cacheType = self.cacheType;
    NSError* error = [self.error copy];
    if (error) {
        dispatch_async(dispatch_get_main_queue(), ^{ completedBlock(nil, nil, error, cacheType, YES, url); });
    } else {
        dispatch_async(dispatch_get_main_queue(), ^{ completedBlock(image, data, nil, cacheType, YES, url); });
    }
    // hack
    return (SDWebImageCombinedOperation*)[[TestSDWebImageOperation alloc] init];
}

@end

@implementation TestSDWebImageOperation

- (void)cancel {
    self.cancelled = YES;
}

@end

