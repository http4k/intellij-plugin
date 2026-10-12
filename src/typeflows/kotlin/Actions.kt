import io.typeflows.github.workflow.step.marketplace.SetupJava
import org.http4k.typeflows.GithubActionConstants.JAVA_VERSION
import org.http4k.typeflows.GithubActionConstants.JDK
import org.http4k.typeflows.GithubActionConstants.SETUP_JAVA

object Actions {
    const val JUNIT_REPORT = "mikepenz/action-junit-report@6ef3fdcfcd5f0b5d14df3e659ef19b8c6820f93d" // v6.6.1

    val SetupJavaAction = SetupJava(JDK, JAVA_VERSION, SETUP_JAVA) {
        with += mapOf("cache" to "gradle")
    }
}
