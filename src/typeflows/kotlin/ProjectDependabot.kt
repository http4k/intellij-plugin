import io.typeflows.github.dependabot.DepdendabotSchedule
import io.typeflows.github.dependabot.Dependabot
import io.typeflows.github.dependabot.PackageEcosystem.GitHubActions
import io.typeflows.github.dependabot.ScheduleInterval.Monthly
import io.typeflows.github.dependabot.Update
import io.typeflows.util.Builder

class ProjectDependabot : Builder<Dependabot> {
    override fun build() = Dependabot.Companion {
        updates += Update(GitHubActions) {
            directory = "/"
            schedule = DepdendabotSchedule(Monthly)
        }
    }
}
