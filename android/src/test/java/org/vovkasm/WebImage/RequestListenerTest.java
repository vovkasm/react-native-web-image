package org.vovkasm.WebImage;

import android.graphics.Bitmap;

import com.bumptech.glide.load.model.GlideUrl;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.CatalystInstance;
import com.facebook.react.bridge.JavaOnlyArray;
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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest(Arguments.class)
public class RequestListenerTest {
    @Rule
    public PowerMockRule rule = new PowerMockRule();

    private ReactApplicationContext appContext;
    private ThemedReactContext themedReactContext;
    private RCTEventEmitter eventEmitter;
    private WebImageViewManager viewManager;

    @Before
    public void setUp () {
        PowerMockito.mockStatic(Arguments.class);

        PowerMockito.when(Arguments.createArray()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return new JavaOnlyArray();
            }
        });
        PowerMockito.when(Arguments.createMap()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return new JavaOnlyMap();
            }
        });

        eventEmitter = mock(RCTEventEmitter.class);

        appContext = new ReactApplicationContext(RuntimeEnvironment.application);
        appContext.initializeWithInstance(createMockCatalystInstance());

        themedReactContext = new ThemedReactContext(appContext, appContext);
        viewManager = new WebImageViewManager();
    }

    @Test
    public void testRequestListenerOnLoad () {
        WebImageView imageView = viewManager.createViewInstance(themedReactContext);
        imageView.setImageUri(new GlideUrl("http://fake/favicon.png"));

        verify(eventEmitter, never()).receiveEvent(anyInt(), anyString(), any(WritableMap.class));

        WebImageView.WebImageViewTarget target = new WebImageView.WebImageViewTarget(imageView);
        Bitmap bitmap = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);

        viewManager.mRequestListener.onResourceReady(bitmap, null, target, null, true);

        WritableMap expectedSource = Arguments.createMap();
        expectedSource.putString("uri", "http://fake/favicon.png");
        expectedSource.putInt("width", 64);
        expectedSource.putInt("height", 64);
        WritableMap expectedEvent = Arguments.createMap();
        expectedEvent.putMap("source", expectedSource);

        verify(eventEmitter).receiveEvent(-1, "onWebImageLoad", expectedEvent);
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
                .thenReturn(eventEmitter);

        return reactInstance;
    }
}
