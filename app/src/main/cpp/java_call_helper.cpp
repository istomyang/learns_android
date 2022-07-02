//
// Created by 杨洋 on 30/5/2022.
//

#include "java_call_helper.h"
#include "f_macro.h"

JavaCallHelper::JavaCallHelper(JavaVM *vm, JNIEnv *env, jobject &obj) : javaVm_(vm), jniEnv_(env) {
    jObject_ = env->NewGlobalRef(obj);
    jclass clazz = env->GetObjectClass(jObject_);

    // 绑定Java函数调用
    jm_errorId_ = env->GetMethodID(clazz, "onError", "(I)V");
    jm_prepareId_ = env->GetMethodID(clazz, "onPrepare", "()V");
    jm_progressId_ = env->GetMethodID(clazz, "onProgress", "(I)V");
}

JavaCallHelper::~JavaCallHelper() {
    jniEnv_->DeleteGlobalRef(jObject_);
    jObject_ = nullptr;
}

void JavaCallHelper::OnError(int thread, int code) const {
    if (thread == THREAD_CHILD) {
        JNIEnv *env = nullptr;
        if (javaVm_->AttachCurrentThread(&env, nullptr) != JNI_OK) { // fail
            return;
        }
        env->CallVoidMethod(jObject_, jm_errorId_, code);
        javaVm_->DetachCurrentThread();
    } else {
        jniEnv_->CallVoidMethod(jObject_, jm_errorId_, code);
    }
}

void JavaCallHelper::OnPrepare(int thread) const {
    if (thread == THREAD_CHILD) {
        JNIEnv *env = nullptr;
        if (javaVm_->AttachCurrentThread(&env, nullptr) != JNI_OK) { // fail
            return;
        }
        env->CallVoidMethod(jObject_, jm_prepareId_);
        javaVm_->DetachCurrentThread();
    } else {
        jniEnv_->CallVoidMethod(jObject_, jm_prepareId_);
    }
}

void JavaCallHelper::OnProgress(int thread, int progress) const {
    if (thread == THREAD_CHILD) {
        JNIEnv *env = nullptr;
        if (javaVm_->AttachCurrentThread(&env, nullptr) != JNI_OK) { // fail
            return;
        }
        env->CallVoidMethod(jObject_, jm_progressId_, progress);
        javaVm_->DetachCurrentThread();
    } else {
        jniEnv_->CallVoidMethod(jObject_, jm_progressId_, progress);
    }
}