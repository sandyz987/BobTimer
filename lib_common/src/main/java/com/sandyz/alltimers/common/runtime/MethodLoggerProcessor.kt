//package com.sandyz.alltimers.common.runtime
//
//import com.squareup.kotlinpoet.FileSpec
//import com.squareup.kotlinpoet.FunSpec
//import com.squareup.kotlinpoet.KModifier
//import java.io.File
//
//import javax.annotation.processing.AbstractProcessor
//
///**
// *@author zhangzhe
// *@date 2023/11/7
// *@description
// */
//
//
////@SupportedAnnotationTypes("MethodLogger")
////@SupportedSourceVersion(SourceVersion.RELEASE_8)
//class MethodLoggerProcessor : AbstractProcessor() {
//
//    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
//        for (element in roundEnv.getElementsAnnotatedWith(MethodLogger::class.java)) {
//            if (element.kind == ElementKind.CLASS) {
//                processClass(element as TypeElement)
//            }
//        }
//        return true
//    }
//
//    private fun processClass(typeElement: TypeElement) {
//        val className = typeElement.qualifiedName.toString()
//        for (enclosedElement in typeElement.enclosedElements) {
//            if (enclosedElement.kind == ElementKind.METHOD) {
//                val methodName = enclosedElement.simpleName.toString()
//                addPrintlnToMethod(className, methodName)
//            }
//        }
//    }
//
//    private fun addPrintlnToMethod(className: String, methodName: String) {
//        val fileBuilder = FileSpec.builder("", "${className}MethodLogger")
//        val funBuilder = FunSpec.builder(methodName)
//            .addModifiers(KModifier.PUBLIC)
//            .addStatement("""println("Method invoked: $methodName")""")
//        fileBuilder.addFunction(funBuilder.build())
//        val file = fileBuilder.build()
//        val kaptKotlinGeneratedDir = processingEnv.options["kapt.kotlin.generated"]
//        file.writeTo(File(kaptKotlinGeneratedDir, "${className}MethodLogger.kt"))
//    }
//}