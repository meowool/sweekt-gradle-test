package com.meowool.sweekt.gradle.job

import actions.core.info
import com.meowool.sweekt.gradle.model.Context
import com.meowool.sweekt.gradle.model.Semver
import com.meowool.sweekt.gradle.service.BotService.Companion.ChangedPrefix
import com.meowool.sweekt.gradle.service.BotService.Companion.UnchangedPrefix
import com.meowool.sweekt.gradle.service.GitService
import com.meowool.sweekt.gradle.service.GithubRepositoryService
import com.meowool.sweekt.gradle.utils.copyDirectory
import com.meowool.sweekt.gradle.utils.onSuccess
import com.meowool.sweekt.gradle.utils.temporaryDirectory
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.toList
import node.path.path
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

/**
 * @author chachako
 */
class SyncUpstreamTagsJob(
  private val git: GitService,
  private val currentRepo: GithubRepositoryService,
) : BotJob(), KoinComponent {
  private val upstreamContext = get<Context>().copy(
    repositoryWithOwner = "gradle/gradle",
  )
  private val upstreamRepo = get<GithubRepositoryService> {
    parametersOf(upstreamContext)
  }

  override suspend fun start(input: BotJobData): BotJobResult {
    val upstreamTags = upstreamRepo.tags
    val currentBranches = currentRepo.branches.toList()
    val tempGithubDirectory = path.resolve(temporaryDirectory, ".github")

    upstreamTags.onStart {
      // Save ".github" directory
      copyDirectory(".github", tempGithubDirectory)
      // Add upstream remote
      git.addRemote("upstream", upstreamContext.repositoryUrl)
    }.onEmpty {
      info("🔫 No tags found in the upstream repository that need to be sync.")
    }.onSuccess {
      info("🔫 Sync upstream tags completed.")
    }.collect { upstreamTag ->
      // The checked out branch is in the format of "upstream/<tag-name>"
      val tagBranch = "upstream/$upstreamTag"
      // Make sure that this tag is not checked out and that this tag version
      // is what we want
      val isNew = Semver(upstreamTag) > Minimum
      val isCheckedOut = currentBranches.any {
        it.name == tagBranch ||
          // We must also consider the merged or failed branches,
          // which are also checked out
          it.name == "$ChangedPrefix/$upstreamTag" ||
          it.name == "$UnchangedPrefix/$upstreamTag"
      }
      if (isNew && !isCheckedOut) {
        val fetchedTag = "fetched-$upstreamTag"
        // Fetch tag from upstream to a new local branch and push
        git.fetch(
          remote = "upstream",
          source = "+refs/tags/$upstreamTag",
          destination = "refs/tags/$fetchedTag",
          options = arrayOf("--no-tags"), // Don't fetch all tags
        ).checkout(
          branch = "tags/$fetchedTag",
          newBranch = tagBranch,
        )
        // Add the ".github" directory, otherwise subsequent workflows
        // will not be triggered
        copyDirectory(tempGithubDirectory, ".github")
        git.commit(
          path = ".github",
          message = "feat(ci): add workflows for auto syncing upstream",
          description = "See [README]($repositoryUrl/blob/-/.github/workflows/bot/readme.md) for more details.", // ktlint-disable max-line-length
        ).push()
      } else if (isCheckedOut) {
        info("💫 Skip tag '$upstreamTag' (already checked out).")
      }
    }

    return jobSuccess()
  }

  companion object {
    /** We need to synchronize all versions larger than `7.5.1`. */
    private val Minimum = Semver("7.5.1")
  }
}
