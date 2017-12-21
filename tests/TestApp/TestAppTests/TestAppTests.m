#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>

#import <SDWebImage/SDWebImageManager.h>
#import <WebImage/WebImageView.h>

@interface TestSDWebImageManager : SDWebImageManager
@property (nonatomic) SDImageCacheType cacheType;
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

- (void)testShouldLoadImage {
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

- (instancetype)init {
    self = [super init];
    if (self) {
        _cacheType = SDImageCacheTypeNone;
    }
    return self;
}

- (nullable id <SDWebImageOperation>)loadImageWithURL:(nullable NSURL *)url options:(SDWebImageOptions)options progress:(nullable SDWebImageDownloaderProgressBlock)progressBlock
 completed:(nullable SDInternalCompletionBlock)completedBlock {
    NSString* sampleBase64 = @"iVBORw0KGgoAAAANSUhEUgAAAEAAAABAAgMAAADXB5lNAAAACVBMVEUAzAD///9mZmaF427XAAAAMElEQVQ4y2NYFYoKGcgQQOOvIkdgMLmDAQrgtpAoADYICuC2kCgw6o7B647R/AKHAAPjfpCz9iukAAAAAElFTkSuQmCC";
    NSData* data = [[NSData alloc] initWithBase64EncodedString:sampleBase64 options:0];
    UIImage* image = [UIImage imageWithData:data];
    SDImageCacheType cacheType = self.cacheType;
    dispatch_async(dispatch_get_main_queue(), ^{
        completedBlock(image, data, nil, cacheType, YES, url);
    });
    return [[TestSDWebImageOperation alloc] init];
}

@end

@implementation TestSDWebImageOperation

- (void)cancel {
    self.cancelled = YES;
}

@end

