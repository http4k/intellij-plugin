package tech.stonks.kvizard.generator

class KtorKoinTreeGenerator: TreeGenerator(
    "ktorkoin",
    jvmResourcesFiles = arrayOf(
        "application.conf",
        "logback.xml"
    ),
    jvmFiles = arrayOf(
        "Main.kt",
        "Service.kt"
    )
)