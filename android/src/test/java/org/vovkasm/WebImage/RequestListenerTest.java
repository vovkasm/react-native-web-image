package org.vovkasm.WebImage;

import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.facebook.react.bridge.CatalystInstance;
import com.facebook.react.bridge.JavaOnlyMap;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.queue.MessageQueueThreadSpec;
import com.facebook.react.bridge.queue.QueueThreadExceptionHandler;
import com.facebook.react.bridge.queue.ReactQueueConfiguration;
import com.facebook.react.bridge.queue.ReactQueueConfigurationImpl;
import com.facebook.react.bridge.queue.ReactQueueConfigurationSpec;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class RequestListenerTest {
    private RCTEventEmitter mEventEmitter;
    private ReactApplicationContext mApplicationContext;
    private ThemedReactContext mThemedReactContext;
    private WebImageViewManager mViewManager;
    private WebImageView mImageView;

    @Before
    public void setUp () {
        mEventEmitter = mock(RCTEventEmitter.class);

        mApplicationContext = new ReactApplicationContext(RuntimeEnvironment.application);
        mApplicationContext.initializeWithInstance(createMockCatalystInstance());

        mThemedReactContext = new ThemedReactContext(mApplicationContext, mApplicationContext);
        mViewManager = new WebImageViewManager();
        mViewManager.mRequestListener = spy(mViewManager.mRequestListener);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return new JavaOnlyMap();
            }
        }).when(mViewManager.mRequestListener).createMap();

        mImageView = mViewManager.createViewInstance(mThemedReactContext);
        mImageView.setImageUri(new GlideUrl("http://fake/favicon.png"));
    }

    @Test
    public void testOnLoadEventEmitted () {
        verify(mEventEmitter, never()).receiveEvent(anyInt(), anyString(), any(WritableMap.class));

        WebImageViewTarget target = new WebImageViewTarget(mImageView);
        Bitmap bitmap = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);

        mViewManager.mRequestListener.onResourceReady(bitmap, null, target, null, true);

        WritableMap expectedSource = new JavaOnlyMap();
        expectedSource.putString("uri", "http://fake/favicon.png");
        expectedSource.putInt("width", 64);
        expectedSource.putInt("height", 64);
        WritableMap expectedEvent = new JavaOnlyMap();
        expectedEvent.putMap("source", expectedSource);

        verify(mEventEmitter).receiveEvent(-1, "onWebImageLoad", expectedEvent);
    }

    @Test
    public void testOnErrorEventEmitted () {
        verify(mEventEmitter, never()).receiveEvent(anyInt(), anyString(), any(WritableMap.class));

        WebImageViewTarget target = new WebImageViewTarget(mImageView);

        mViewManager.mRequestListener.onLoadFailed(new GlideException("some error"), null, target, true);

        WritableMap expectedEvent = new JavaOnlyMap();
        expectedEvent.putString("uri", "http://fake/favicon.png");
        expectedEvent.putString("error", "some error");

        verify(mEventEmitter).receiveEvent(-1, "onWebImageError", expectedEvent);
    }

    public CatalystInstance createMockCatalystInstance() {
        ReactQueueConfigurationSpec spec = ReactQueueConfigurationSpec.builder()
                .setJSQueueThreadSpec(MessageQueueThreadSpec.mainThreadSpec())
                .setNativeModulesQueueThreadSpec(MessageQueueThreadSpec.mainThreadSpec())
                .build();
        ReactQueueConfiguration ReactQueueConfiguration = ReactQueueConfigurationImpl.create(
                spec,
                new QueueThreadExceptionHandler() {
                    @Override
                    public void handleException(Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        CatalystInstance reactInstance = mock(CatalystInstance.class);
        when(reactInstance.getReactQueueConfiguration()).thenReturn(ReactQueueConfiguration);
        when(reactInstance.getNativeModule(UIManagerModule.class))
                .thenReturn(mock(UIManagerModule.class));
        when(reactInstance.getJSModule(RCTEventEmitter.class))
                .thenReturn(mEventEmitter);

        return reactInstance;
    }
}
