import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType

dependencies {
    labyProcessor()
    api(project(":api"))

    // An example of how to add an external dependency that is used by the addon.
    // addonMavenDependency("org.jeasy:easy-random:5.0.0")

    // https://mvnrepository.com/artifact/org.projectlombok/lombok
//    compileOnly("org.projectlombok:lombok:1.18.28")
//    annotationProcessor("org.projectlombok:lombok:1.18.28")
//
//    testCompileOnly("org.projectlombok:lombok:1.18.28")
//    testAnnotationProcessor("org.projectlombok:lombok:1.18.28")

//    addonMavenDependency("org.projectlombok", "lombok", "1.18.28")
}

labyModAnnotationProcessor {
    referenceType = ReferenceType.DEFAULT
}
