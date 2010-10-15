#include <jni.h>
#ifdef LOG
#include <android/log.h>
#endif

#include <Box2D/Box2D.h>
#include <vector>

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jboolean 	JNICALL Java_com_android_box2d_Box2D_createWorld			(JNIEnv *env, jclass cls, jobject gravity, jboolean doSleep);
JNIEXPORT jint 		JNICALL Java_com_android_box2d_Box2D_createBody				(JNIEnv *env, jclass cls, jobject bodyDef);
JNIEXPORT void 		JNICALL Java_com_android_box2d_Box2D_setBodyDef				(JNIEnv *env, jclass cls, jint bodyId, jobject bodyDef);

JNIEXPORT jint 		JNICALL Java_com_android_box2d_Box2D_createShape			(JNIEnv *env, jclass cls, jint shapeType);
JNIEXPORT void 		JNICALL Java_com_android_box2d_Box2D_setPolygonShapeAsBox(JNIEnv *env, jclass cls, jint shapeId, jfloat hx, jfloat hy);

JNIEXPORT void 		JNICALL Java_com_android_box2d_Box2D_createFixture 		(JNIEnv *env, jclass cls, jint bodyId, jint shapeId, jint density);

#ifdef __cplusplus
}
#endif

using namespace std;

b2World *mWorld=0;
vector<b2BodyDef*> mBodiesDef;
vector<b2Body*> mBodies;
vector<b2Shape*> mShapes;
vector<b2Fixture*> mFixtures;

//-----------------------------------------------------------------------------
void getVec2 (JNIEnv *env, b2Vec2 &v, jobject obj)
{
   static jclass vec2Class = 0;
   if (vec2Class==0)
   	vec2Class=env->GetObjectClass(obj);
  	v.x=env->GetFloatField(obj,env->GetFieldID(vec2Class, "x", "F"));
  	v.y=env->GetFloatField(obj,env->GetFieldID(vec2Class, "y", "F"));
#ifdef LOG
	__android_log_print(ANDROID_LOG_VERBOSE, "Box2D", "x=%f",v.x);
	__android_log_print(ANDROID_LOG_VERBOSE, "Box2D", "y=%f",v.y);
#endif
}
//-----------------------------------------------------------------------------

JNIEXPORT jboolean JNICALL Java_com_android_box2d_Box2D_createWorld(JNIEnv *env, jclass cls, jobject gravity, jboolean doSleep)
{
	b2Vec2 vg;
  	getVec2(env, vg, gravity);
	mWorld=new b2World(vg, doSleep);

#ifdef LOG
	__android_log_print(ANDROID_LOG_INFO, "Box2D", "createWorld");
#endif
}

//-----------------------------------------------------------------------------

JNIEXPORT jint JNICALL Java_com_android_box2d_Box2D_createBody(JNIEnv *env, jclass cls, jobject def)
{
	int bodyId=mBodies.size();
	b2BodyDef *bodyDef=new b2BodyDef();
	b2Body *body=mWorld->CreateBody(bodyDef);
	mBodiesDef.push_back(bodyDef);
	mBodies.push_back(body);

	Java_com_android_box2d_Box2D_setBodyDef(env, cls, bodyId, def);

#ifdef LOG
	__android_log_print(ANDROID_LOG_INFO, "Box2D", "createBody");
#endif

	return bodyId;
}

JNIEXPORT void JNICALL Java_com_android_box2d_Box2D_setBodyDef(JNIEnv *env, jclass cls, jint bodyId, jobject def)
{
	b2BodyDef *bodyDef=mBodiesDef[bodyId];

   jclass defClass = env->GetObjectClass(def);
  	bodyDef->type=(b2BodyType)env->GetIntField(def,env->GetFieldID(defClass, "type", "I"));
  	getVec2(env, bodyDef->position, env->GetObjectField(def,env->GetFieldID(defClass, "position", "Lcom/android/box2d/Vec2;")));
  	bodyDef->angle=env->GetFloatField(def,env->GetFieldID(defClass, "angle", "F"));
  	getVec2(env, bodyDef->linearVelocity, env->GetObjectField(def,env->GetFieldID(defClass, "linearVelocity", "Lcom/android/box2d/Vec2;")));
  	bodyDef->angularVelocity=env->GetFloatField(def,env->GetFieldID(defClass, "angularVelocity", "F"));
  	bodyDef->linearDamping=env->GetFloatField(def,env->GetFieldID(defClass, "linearDamping", "F"));
  	bodyDef->angularDamping=env->GetFloatField(def,env->GetFieldID(defClass, "angularDamping", "F"));
  	bodyDef->allowSleep=env->GetBooleanField(def,env->GetFieldID(defClass, "allowSleep", "Z"));
  	bodyDef->awake=env->GetBooleanField(def,env->GetFieldID(defClass, "awake", "Z"));
  	bodyDef->fixedRotation=env->GetBooleanField(def,env->GetFieldID(defClass, "fixedRotation", "Z"));
  	bodyDef->bullet=env->GetBooleanField(def,env->GetFieldID(defClass, "bullet", "Z"));
  	bodyDef->active=env->GetBooleanField(def,env->GetFieldID(defClass, "active", "Z"));
  	bodyDef->inertiaScale=env->GetFloatField(def,env->GetFieldID(defClass, "inertiaScale", "F"));
#ifdef LOG
	__android_log_print(ANDROID_LOG_INFO, "Box2D", "defineBody");
#endif
}

//-----------------------------------------------------------------------------

#define stCircle 	0
#define stEdge		1
#define stLoop		2
#define stPolygon	3

JNIEXPORT jint JNICALL Java_com_android_box2d_Box2D_createShape(JNIEnv *env, jclass cls, jint shapeType)
{
	int id=mShapes.size();
	b2Shape *shape=0;
	switch (shapeType)
	{
		case stCircle: 	shape=new b2CircleShape(); 	break;
		case stEdge: 		shape=new b2EdgeShape(); 		break;
		case stLoop: 		shape=new b2LoopShape(); 		break;
		case stPolygon:	shape=new b2PolygonShape();	break;
	}
	if (shape!=0)
	{
		mShapes.push_back(shape);
#ifdef LOG
	__android_log_print(ANDROID_LOG_INFO, "Box2D", "createShape");
#endif
		return id;
	}
	return -1;
}

JNIEXPORT void JNICALL Java_com_android_box2d_Box2D_setPolygonShapeAsBox(JNIEnv *env, jclass cls, jint shapeId, jfloat hx, jfloat hy)
{
	b2PolygonShape *shape=(b2PolygonShape *)mShapes[shapeId];
	shape->SetAsBox(hx,hy);

#ifdef LOG
	__android_log_print(ANDROID_LOG_INFO, "Box2D", "setPolygonShapeAsBox");
#endif
}

//-----------------------------------------------------------------------------

JNIEXPORT void JNICALL Java_com_android_box2d_Box2D_createFixture (JNIEnv *env, jclass cls, jint bodyId, jint shapeId, jint density)
{
	b2Body *body=mBodies[bodyId];
	b2Shape *shape=mShapes[shapeId];
	body->CreateFixture(shape,density);

#ifdef LOG
	__android_log_print(ANDROID_LOG_INFO, "Box2D", "createShape");
#endif
}
