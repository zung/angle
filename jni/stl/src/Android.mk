LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := libstlport

LOCAL_SRC_FILES := \
   stl/src/cxa.c \
   stl/src/c_locale.c \
   stl/src/dll_main.cpp \
   stl/src/fstream.cpp \
   stl/src/strstream.cpp \
   stl/src/sstream.cpp \
   stl/src/ios.cpp \
   stl/src/stdio_streambuf.cpp \
   stl/src/istream.cpp \
   stl/src/ostream.cpp \
   stl/src/iostream.cpp \
   stl/src/codecvt.cpp \
   stl/src/collate.cpp \
   stl/src/ctype.cpp \
   stl/src/monetary.cpp \
   stl/src/num_get.cpp \
   stl/src/num_put.cpp \
   stl/src/num_get_float.cpp \
   stl/src/num_put_float.cpp \
   stl/src/numpunct.cpp \
   stl/src/time_facets.cpp \
   stl/src/messages.cpp \
   stl/src/facets_byname.cpp \
   stl/src/complex.cpp \
   stl/src/complex_io.cpp \
   stl/src/complex_trig.cpp \
   stl/src/string.cpp \
   stl/src/bitset.cpp \
   stl/src/allocators.cpp \
   stl/src/locale_impl.cpp \
   stl/src/locale_catalog.cpp

#LOCAL_NO_DEFAULT_COMPILER_FLAGS := true

LOCAL_CFLAGS := \
    $(TARGET_GLOBAL_CFLAGS) \
    $(TARGET_arm_CFLAGS) \
   -D_GNU_SOURCE \
   -fvisibility=hidden

LOCAL_CPPFLAGS := \
    $(TARGET_GLOBAL_CPPFLAGS) \
	-D_STLP_DONT_USE_EXCEPTIONS \
   -fuse-cxa-atexit

LOCAL_C_INCLUDES := \
    $(LOCAL_PATH)/stl/stlport \
    $(LOCAL_C_INCLUDES) \
    $(TARGET_PROJECT_INCLUDES) \
    $(TARGET_C_INCLUDES)

LOCAL_LDFLAGS := \
    -Wl,--no-undefined \
    $(LOCAL_LDFLAGS)
    
#include $(BUILD_SHARED_LIBRARY)
include $(BUILD_STATIC_LIBRARY)
