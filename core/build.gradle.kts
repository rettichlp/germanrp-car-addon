import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType

dependencies {
    labyProcessor()
    api(project(":api"))

    // An example of how to add an external dependency that is used by the addon.
    // addonMavenDependency("org.jeasy:easy-random:5.0.0")

    // https://mvnrepository.com/artifact/org.projectlombok/lombok
    addonMavenDependency("org.projectlombok", "lombok", "1.18.38")

    compileOnly("org.projectlombok", "lombok", "1.18.34")
    annotationProcessor("org.projectlombok", "lombok", "1.18.38")

    testCompileOnly("org.projectlombok", "lombok", "1.18.38")
    testAnnotationProcessor("org.projectlombok", "lombok", "1.18.38")
}

labyModAnnotationProcessor {
    referenceType = ReferenceType.DEFAULT
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
