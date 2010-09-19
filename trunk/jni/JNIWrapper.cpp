#include <jni.h>
#ifdef LOG
#include <android/log.h>
#endif

#include <Box2D/Box2D.h>

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jboolean JNICALL Java_com_android_angle_Box2D_createWorld	(JNIEnv *env, jobject obj, jfloat gravityX, jfloat gravityY, jboolean doSleep);

#ifdef __cplusplus
}
#endif

b2World *mWorld;

//-----------------------------------------------------------------------------

JNIEXPORT jboolean JNICALL Java_com_android_angle_Box2D_createWorld(JNIEnv *env, jobject obj, jfloat gravityX, jfloat gravityY, jboolean doSleep)
{
	mWorld=new b2World(b2Vec2(gravityX,gravityY), doSleep);

#ifdef LOG
	__android_log_print(ANDROID_LOG_INFO, "Box2D", "createWorld");
#endif
}

