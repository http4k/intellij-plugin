import io.typeflows.github.DotGitHub
import io.typeflows.github.TypeflowsGitHubRepo
import io.typeflows.github.visualisation.WorkflowVisualisations
import io.typeflows.github.workflow.Cron
import io.typeflows.github.workflow.step.RunCommand
import io.typeflows.util.Builder
import org.http4k.typeflows.GithubActionConstants.JAVA_VERSION
import org.http4k.typeflows.Http4kProjectStandards
import org.http4k.typeflows.UpdateGradleProjectDependencies

class Typeflows : Builder<TypeflowsGitHubRepo> {
    override fun build() = TypeflowsGitHubRepo {
        dotGithub = DotGitHub {
            workflows += BuildWorkflow()
            workflows += ReleaseWorkflow()
            workflows += UpdateGradleProjectDependencies(
                "update-dependencies",
                Cron.of("0 08 * * 1"),
                RunCommand("./gradlew check"),
                "main",
                "auto-update",
                JAVA_VERSION
            )

            dependabot = ProjectDependabot().build()

            files += WorkflowVisualisations(workflows)
        }

        files += Http4kProjectStandards()
    }
}
