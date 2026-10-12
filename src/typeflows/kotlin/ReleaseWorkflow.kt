import Actions.SetupJavaAction
import io.typeflows.github.workflow.Job
import io.typeflows.github.workflow.Permission.Contents
import io.typeflows.github.workflow.Permission.PullRequests
import io.typeflows.github.workflow.PermissionLevel.Write
import io.typeflows.github.workflow.Permissions
import io.typeflows.github.workflow.RunsOn.Companion.UBUNTU_LATEST
import io.typeflows.github.workflow.Secrets
import io.typeflows.github.workflow.Tag
import io.typeflows.github.workflow.Workflow
import io.typeflows.github.workflow.step.RunCommand
import io.typeflows.github.workflow.step.marketplace.Checkout
import io.typeflows.github.workflow.step.marketplace.SetupGradle
import io.typeflows.github.workflow.trigger.Push
import io.typeflows.util.Builder
import org.http4k.typeflows.GithubActionConstants.CHECKOUT
import org.http4k.typeflows.GithubActionConstants.SETUP_GRADLE

class ReleaseWorkflow : Builder<Workflow> {
    override fun build() = Workflow("release") {
        displayName = "Release"
        on += Push {
            tags += Tag.of("*")
        }

        jobs += Job("release", UBUNTU_LATEST) {
            name = "Publish Plugin"
            permissions = Permissions(Contents to Write, PullRequests to Write)

            steps += Checkout(CHECKOUT)
            steps += SetupJavaAction
            steps += SetupGradle(SETUP_GRADLE)
            steps += RunCommand("./gradlew publishPlugin") {
                name = "Publish Plugin"
                env["JETBRAINS_TOKEN"] = Secrets.string("JETBRAINS_TOKEN")
            }
        }
    }
}
