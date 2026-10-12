import Actions.JUNIT_REPORT
import Actions.SetupJavaAction
import io.typeflows.github.workflow.Conditions.always
import io.typeflows.github.workflow.Job
import io.typeflows.github.workflow.RunsOn.Companion.UBUNTU_LATEST
import io.typeflows.github.workflow.Secrets
import io.typeflows.github.workflow.Workflow
import io.typeflows.github.workflow.step.RunCommand
import io.typeflows.github.workflow.step.UseAction
import io.typeflows.github.workflow.step.marketplace.Checkout
import io.typeflows.github.workflow.step.marketplace.SetupGradle
import io.typeflows.github.workflow.trigger.Push
import io.typeflows.util.Builder
import org.http4k.typeflows.GithubActionConstants.CHECKOUT
import org.http4k.typeflows.GithubActionConstants.SETUP_GRADLE

class BuildWorkflow : Builder<Workflow> {
    override fun build() = Workflow("build") {
        displayName = "Build"
        on += Push()

        jobs += Job("build", UBUNTU_LATEST) {
            steps += Checkout(CHECKOUT) {
                fetchDepth = 2
                persistCredentials = false
            }
            steps += SetupJavaAction
            steps += SetupGradle(SETUP_GRADLE)
            steps += RunCommand("./gradlew check") {
                name = "Build"
                timeoutMinutes = 15
            }
            steps += UseAction(JUNIT_REPORT) {
                name = "Publish Test Report"
                condition = always()
                with += mapOf(
                    "report_paths" to "**/build/test-results/test/TEST-*.xml",
                    "github_token" to Secrets.GITHUB_TOKEN.toString()
                )
            }
        }
    }
}
