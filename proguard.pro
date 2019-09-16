#### AUTO-GENERATED PROGUARD RULE FOR stetho START ####
#-keep class com.facebook.stetho.** { *; }
#-dontwarn com.facebook.stetho.**
#### AUTO-GENERATED PROGUARD RULE FOR stetho END   ####

-dontnote
-dontwarn
#-dontskipnonpubliclibraryclasses
#-dontskipnonpubliclibraryclassmembers
#-optimizations !code/simplification/cast,!field/*,!class/merging/*

#### AUTO-GENERATED PROGUARD RULE FOR gson START ####
## GSON 2.2.4 specific rules ##

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
#-keepattributes Signature
#
## For using GSON @Expose annotation
#-keepattributes *Annotation*
#
#-keepattributes EnclosingMethod
#-keepattributes *Annotation*,InnerClasses
# Gson specific classes
#-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
#### AUTO-GENERATED PROGUARD RULE FOR gson END   ####


#### AUTO-GENERATED PROGUARD RULE FOR retrofit START ####
#-dontwarn retrofit2.**
#-keep class retrofit2.** { *; }
#-keepattributes Signature
#-keepattributes Exceptions

-keepclasseswithmembers class *
#-keepclassmembers class *
#-keep public class * extends com.intellij.openapi.actionSystem.AnAction
##-keepclasseswithmembernames class * {
##    native <methods>;
##}
##代码混淆压缩比，在0~7之间，默认为5，一般不做修改
#-optimizationpasses 5
##指定混淆是采用的算法，后面的参数是一个过滤器，这个过滤器是谷歌推荐的算法，一般不做更改
#-optimizations !code/simplification/cast,!field/*,!class/merging/*
#
##混合时不使用大小写混合，混合后的类名为小写,windows下必须使用该选项
#-dontusemixedcaseclassnames
#
##指定不去忽略非公共库的类和成员
#-dontskipnonpubliclibraryclasses
#-dontskipnonpubliclibraryclassmembers
#
##输出详细信息
#-verbose
##输出类名->混淆后类名的映射关系
#-printmapping map.txt
#
##不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
#-dontpreverify
#
##保留Annotation不混淆
#-keepattributes *Annotation*,InnerClasses
#
##避免混淆泛型
#-keepattributes Signature
#
##抛出异常时保留代码行号
#-keepattributes SourceFile,LineNumberTable
#
##保留本地native方法不被混淆
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#
##保留枚举类不被混淆
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
##保留Serializable序列化的类不被混淆
#-keepclassmembers class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}


## Platform calls Class.forName on types which do not exist on Android to determine platform.
#-dontnote retrofit2.Platform
## Platform used when running on RoboVM on iOS. Will not be used at runtime.
#-dontnote retrofit2.Platform$IOS$MainThreadExecutor
## Platform used when running on Java 8 VMs. Will not be used at runtime.
#-dontwarn retrofit2.Platform$Java8
#
#-dontwarn okio.**
#-dontwarn javax.annotation.**
##### AUTO-GENERATED PROGUARD RULE FOR retrofit END   ####
#
#
##### AUTO-GENERATED PROGUARD RULE FOR rxjava START ####
## Rxjava-promises
#
#-keep class com.darylteo.rx.** { *; }
#
#-dontwarn com.darylteo.rx.**
##### AUTO-GENERATED PROGUARD RULE FOR rxjava END   ####
#
#
##### AUTO-GENERATED PROGUARD RULE FOR bottom-bar START ####
#-dontwarn com.roughike.bottombar.**
##### AUTO-GENERATED PROGUARD RULE FOR bottom-bar END   ####
#
#
##### AUTO-GENERATED PROGUARD RULE FOR picasso START ####
### Square Picasso specific rules ##
### https://square.github.io/picasso/ ##
#-dontwarn com.squareup.okhttp.**
#
## Checks for OkHttp versions on the classpath to determine Downloader to use.
#-dontnote com.squareup.picasso.Utils
## Downloader used only when OkHttp 2.x is present on the classpath.
#-dontwarn com.squareup.picasso.OkHttpDownloader
## Downloader used only when OkHttp 3.x is present on the classpath.
#-dontwarn com.squareup.picasso.OkHttp3Downloader
##### AUTO-GENERATED PROGUARD RULE FOR picasso END   ####

#-dontwarn com.intellij.**
#-keep class com.intellij.**

#-dontwarn com.google.**
#-keep class com.google.**
#
#-dontwarn org.jetbrains.**
#-keep class org.jetbrains.**
#
#-dontwarn org.slf4j.**
#-keep class org.slf4j.**
#
#-dontwarn org.apache.**
#-keep class org.apache.**
#
##-dontwarn com.ccnode.codegenerator.action.dialog.**
##-keep class com.ccnode.codegenerator.action.dialog.**
#
#-dontwarn org.junit.**
#-keep class org.junit.**

#-dontwarn dialog.**
#-keep class dialog.**

#-ignorewarnings
-obfuscationdictionary method-dictionary.txt
-packageobfuscationdictionary package-dictionary.txt
-classobfuscationdictionary class-dictionary.txt

#-optimizationpasses 5
#-dontusemixedcaseclassnames
#-dontpreverify
-dontshrink
#-dontoptimize

#-verbose
#-repackageclasses ''
#-allowaccessmodification