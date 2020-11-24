-keepclassmembers class ku.olga.core_impl.repository.model.** {
    <fields>;
    <init>();
}
-keepclassmembers,allowoptimization enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}