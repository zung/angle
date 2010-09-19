LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

include $(LOCAL_PATH)/stl/src/Android.mk 

LOCAL_MODULE := box2d
LOCAL_CFLAGS := -Werror -DLOG
LOCAL_LDLIBS := -ldl -llog
LOCAL_C_INCLUDES := \
    $(LOCAL_PATH)/stl/stlport \
    $(LOCAL_C_INCLUDES) \
    $(TARGET_PROJECT_INCLUDES) \
    $(TARGET_C_INCLUDES)
LOCAL_SRC_FILES := JNIWrapper.cpp
include $(LOCAL_PATH)/Box2D/Collision/_addon.mk 
include $(LOCAL_PATH)/Box2D/Common/_addon.mk 
include $(LOCAL_PATH)/Box2D/Dynamics/_addon.mk 

LOCAL_STATIC_LIBRARIES += libstlport  

include $(BUILD_SHARED_LIBRARY)


