//
// Created by 杨洋 on 30/5/2022.
//

#include "main.h"
#include "macro.h"
#include "java_call_helper.h"

JavaVM *global_vm = nullptr;
JavaCallHelper *javaCallHelper = nullptr;

// 调用System.loadLibrary()函数时， 内部就会去查找so中的 JNI_OnLoad 函数，如果存在此函数则调用。
// https://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/invocation.html#JNI_OnLoad
jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    global_vm = vm;
    return JNI_VERSION_1_6;
}

////////////////////////////////////////////////////////////////////////////
////////////////////////////// DNPlayer ////////////////////////////////////
////////////////////////////////////////////////////////////////////////////


extern "C"
JNIEXPORT void JNICALL
Java_ty_learns_android_ndk_Player_native_1prepare(JNIEnv *env, jobject instance,
                                                  jstring data_source) {
//    const char *dataSource = env->GetStringUTFChars(data_source, 0);
//    javaCallHelper = new JavaCallHelper(global_vm, env, instance);
//    ffmpeg = new DNFFmpeg(javaCallHelper, dataSource);
//    ffmpeg->setRenderCallback(renderFrame);
//    ffmpeg->prepare();
//    env->ReleaseStringUTFChars(dataSource_, dataSource);
}

extern "C"
JNIEXPORT void JNICALL
Java_ty_learns_android_ndk_Player_native_1start(JNIEnv *env, jobject thiz) {

}



////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////

#include <string>

using namespace std;

class HeyNDK {
public:
    static string getGreetWords();
};

string HeyNDK::getGreetWords() {
    return "Hey, NDK!";
}

/*
 * extern "C"：c++中需要以c的方式编译
 * JNIEnv: 由Jvm传入与线程相关的变量。定义了JNI系统操作、java交互等方法。
 * jobject: 表示当前调用对象，即 this , 如果是静态的native方法，则获得jclass
 *
 * JNIEXPORT：在 Windows 中,定义为__declspec(dllexport)。因为Windows编译 dll 动态库规定，如果动态库中的函数要
 * 被外部调用，需要在函数声明中添加此标识，表示将该函数导出在外部可以调用。在 Linux/Unix/Mac os/Android 这种 Like
 * Unix系统中，定义为__attribute__ ((visibility ("default")))
 *
 * GCC 有个visibility属性, 该属性是说, 启用这个属性:
 * 1、当-fvisibility=hidden时动态库中的函数默认是被隐藏的即 hidden. 除非显示声明为__attribute__((visibility("default"))).
 * 2、当-fvisibility=default时动态库中的函数默认是可见的.除非显示声明为__attribute__((visibility("hidden"))).
 *
 * JNICALL：在类Unix中无定义，在Windows中定义为：_stdcall ，一种函数调用约定，类Unix系统中这两个宏可以省略不加。
 * */
extern "C" JNIEXPORT jstring JNICALL
Java_ty_learns_android_MainActivity_getHeyStringFromJNI(
        JNIEnv *env,
        jobject /* this */, jstring name) {

    // 获得c字符串
    // 参数2：用于接收jvm传给我们的字符串是否是拷贝的，通常，我们不关心这个,一般传个NULL就可以
    const char *nameStr = env->GetStringUTFChars(name, JNI_FALSE);

    char resultString[100];

    HeyNDK heyNdk;
    std::string hello = heyNdk.getGreetWords();

    // c_str() 需要转换为 C
    sprintf(resultString, "%s: %s", nameStr, hello.c_str());

    // 必须释放内存
    env->ReleaseStringUTFChars(name, nameStr);

    // 新建一个JVM字符串，返回
    return env->NewStringUTF(resultString);
}
extern "C"
JNIEXPORT void JNICALL
Java_ty_learns_android_ndk_HeyNDK_useDLLManualCPP(JNIEnv *env, jobject thiz) {

}

// 处理数组
extern "C"
JNIEXPORT jobjectArray JNICALL
Java_ty_learns_android_ndk_HeyNDK_getHeyWords(JNIEnv *env, jclass clazz, jobjectArray names) {
    int len = env->GetArrayLength(names);

    jobjectArray ret = env->NewObjectArray(len, env->FindClass("java/lang/String"),
                                           env->NewStringUTF(""));

    for (int i = 0; i < len; i++) {
        auto jStr = static_cast<jstring>(env->GetObjectArrayElement(names, i));
        const char *cStr = env->GetStringUTFChars(jStr, JNI_FALSE);
        char newStr[50];
        sprintf(newStr, "Hey, %s", cStr);
        env->SetObjectArrayElement(ret, i, env->NewStringUTF(newStr));
        env->ReleaseStringUTFChars(jStr, cStr);
    }

    return ret;
}
extern "C"
JNIEXPORT void JNICALL
Java_ty_learns_android_ndk_HeyNDK_showCPPCallJava(JNIEnv *env, jobject it) {
    jclass clazz = env->FindClass("ty/learns/android/ndk/HeyNDK");

    // 获取静态方法
    // 获取参数sig 方式：
    // javap -s HeyNDK
    // 此路径：/learns_android/app/build/intermediates/javac/debug/classes/ty/learns/android/ndk
    jmethodID staticMethod = env->GetStaticMethodID(clazz, "showCPPCallJavaStaticMethod",
                                                    "([Ljava/lang/String;D)V");

    jclass jStringCls = nullptr;
    jStringCls = env->FindClass("java/lang/String"); // 获取String类型
    auto stringArray = env->NewObjectArray(2, jStringCls, nullptr);
    env->SetObjectArrayElement(stringArray, 0, env->NewStringUTF("Tom")); // 设置数组元素
    env->SetObjectArrayElement(stringArray, 1, env->NewStringUTF("Jerry"));
    env->CallStaticVoidMethod(clazz, staticMethod, stringArray, 1.0); // 调用

    jmethodID instanceMethod = env->GetMethodID(clazz, "showCPPCallJavaInstanceMethod",
                                                "(Ljava/lang/String;I)Ljava/lang/String;");
    jmethodID constructMethod = env->GetMethodID(clazz, "<init>", "()V"); // 必须拿到构造器函数
    jobject obj = env->NewObject(clazz, constructMethod);
    auto ret = static_cast<jstring>(env->CallObjectMethod(obj, instanceMethod,
                                                          env->NewStringUTF("Hey!"), 1));
    const char *str1 = env->GetStringUTFChars(ret, JNI_FALSE);
    LOGI("%s", str1); // Tag：HELLO_JNI

    env->ReleaseStringUTFChars(ret, str1); // 牵涉到 GetStringUTFChars 且为 JNI_FALSE
    env->DeleteLocalRef(jStringCls); // 你看一下类型, C++对象就要删除
    env->DeleteLocalRef(stringArray); // 局部应用阻止引用的对象被GC回收
    env->DeleteLocalRef(obj);
}


void *runTask(void *args) {
    JNIEnv *env;
    global_vm->AttachCurrentThread(&env, nullptr);

    // You can use JNiEnv

    global_vm->DetachCurrentThread();
}

#include <pthread.h>

extern "C"
JNIEXPORT void JNICALL
Java_ty_learns_android_ndk_HeyNDK_runOtherThread(JNIEnv *env, jobject thiz) {
    pthread_t pid;
    pthread_create(&pid, nullptr, runTask, nullptr);
}

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libavfilter/avfilter.h>
#include <libavcodec/jni.h>
#include <unistd.h>

JNIEXPORT jstring
Java_ty_learns_android_ndk_HeyFFmpeg_ffmpegInfo(JNIEnv *env, jclass /* thiz */) {

    char info[40000] = {0};
    void *iterate = nullptr;
    for (;;) {
        const AVCodec *c_temp = av_codec_iterate(&iterate);
        if (!c_temp)
            break;
        if (c_temp->decode != nullptr) {
            sprintf(info, "%sdecode:", info);
        } else {
            sprintf(info, "%sencode:", info);
        }
        switch (c_temp->type) {
            case AVMEDIA_TYPE_VIDEO:
                sprintf(info, "%s(video):", info);
                break;
            case AVMEDIA_TYPE_AUDIO:
                sprintf(info, "%s(audio):", info);
                break;
            default:
                sprintf(info, "%s(other):", info);
                break;
        }
        sprintf(info, "%s[%s]\n", info, c_temp->name);
    }

    return env->NewStringUTF(info);
}

}