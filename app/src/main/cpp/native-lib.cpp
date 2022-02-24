#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_sandeep_app_1sharing_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jobject JNICALL
Java_com_example_sandeep_app_1sharing_Fragments_AppFragment_ToJni(JNIEnv *env, jobject thiz, jstring name,
                                                      jlong apk_size, jobject icon,
                                                      jstring apk_path) {



    jclass sampleclass = env->FindClass("com/example/sandeep/app_sharing/App_Data");
    jmethodID methodId = env->GetMethodID(sampleclass,"<init>",
                                          "(Ljava/lang/String;JLandroid/graphics/drawable/Drawable;Ljava/lang/String;)V");
    jobject sampleobj = env->NewObject(sampleclass,methodId,name,apk_size,icon,apk_path);
    return sampleobj;




}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_example_sandeep_app_1sharing_MainActivity_To(JNIEnv *env, jobject thiz, jstring name,
                                                      jlong apk_size, jobject icon,
                                                      jstring apk_path) {
    // TODO: implement To()
}


