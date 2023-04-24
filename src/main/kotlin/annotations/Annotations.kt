package annotations
@Target(AnnotationTarget.PROPERTY)
annotation class Ignore

@Target(AnnotationTarget.PROPERTY)
annotation class ChangeName(val name: String)

@Target(AnnotationTarget.PROPERTY)
annotation class AsJString