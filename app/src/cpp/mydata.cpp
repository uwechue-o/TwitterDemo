//
// Created by Oke Uwechue on 3/16/17.
//

#include <jni.h>

extern "C" {
    /*
     * Class:     com_dmt_twitterdemo_MainActivity
     * Method:    getTwitterSecret
     * Signature: ()Ljava/lang/String;
     */
    JNIEXPORT jstring JNICALL Java_com_dmt_twitterdemo_MainActivity_getTwitterSecret
            (JNIEnv *env, jclass obj) {
        return env->NewStringUTF("<PLACEHOLDER>");
    }

    /*
     * Class:     com_dmt_twitterdemo_MainActivity
     * Method:    getTwitterConsumerKey
     * Signature: ()Ljava/lang/String;
     */
    JNIEXPORT jstring JNICALL Java_com_dmt_twitterdemo_MainActivity_getTwitterConsumerKey
            (JNIEnv *env, jclass obj) {
        return env->NewStringUTF("<PLACEHOLDER>");
    }
}