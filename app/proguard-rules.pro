# Keep all application classes and members intact for R8 shrinking safety
-keep class com.vinaykpro.ludoking.** { *; }

# Keep XML onClick handlers and View constructors
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Glide rules
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
    <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
-dontwarn com.bumptech.glide.**
-dontwarn org.apache.http.**
-dontwarn android.net.http.**