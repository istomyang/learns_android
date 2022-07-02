//
// Created by 杨洋 on 30/5/2022.
//

#ifndef LEARNS_ANDROID_JAVA_CALL_HELPER_H
#define LEARNS_ANDROID_JAVA_CALL_HELPER_H

#include <jni.h>

// JavaCallHelper helps native code to call java code for updating UI.
class JavaCallHelper {
public:
    JavaCallHelper(JavaVM *vm, JNIEnv *env, jobject &obj);

    ~JavaCallHelper();

    void OnError(int thread, int code) const;

    void OnPrepare(int thread) const;

    void OnProgress(int thread, int progress) const;

public:
    JavaVM *javaVm_;
    JNIEnv *jniEnv_;
    jobject jObject_;
    jmethodID jm_errorId_;
    jmethodID jm_prepareId_;
    jmethodID jm_progressId_;

};


#endif //LEARNS_ANDROID_JAVA_CALL_HELPER_H
