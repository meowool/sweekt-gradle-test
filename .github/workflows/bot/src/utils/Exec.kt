package com.meowool.sweekt.gradle.utils

import actions.exec.ExecOptions
import actions.exec.ExecOutput
import actions.exec.getExecOutput
import kotlinx.coroutines.await

class ExecException(
  cause: Throwable,
  commandLine: String,
  args: Array<String>?,
  var exitCode: Number? = null,
  val stdout: String? = null,
  val stderr: String? = null,
) : IllegalStateException(cause) {
  val statement = commandLine + args?.joinToString(
    separator = " ",
    prefix = " ",
  ).orEmpty()

  val log: String? = listOfNotNull(stdout, stderr)
    .joinToString("\n").trim()
    .takeIf { it.isNotBlank() }
}

suspend fun exec(commandLine: String, vararg args: Any?): ExecOutput {
  val options = object : ExecOptions {
    override var ignoreReturnCode: Boolean? = true
  }
  val arguments = args.mapNotNull { it?.toString() }.takeIf { it.isNotEmpty() }
    ?.toTypedArray()

  return runCatching {
    when (arguments) {
      null -> getExecOutput(commandLine, options = options)
      else -> getExecOutput(commandLine, arguments, options)
    }.await()
  }.onSuccess {
    if (it.exitCode != 0) {
      throw ExecException(
        cause = IllegalStateException(
          "The process '$commandLine' failed with exit code ${it.exitCode}",
        ),
        commandLine = commandLine,
        args = arguments,
        exitCode = it.exitCode,
        stdout = it.stdout,
        stderr = it.stderr,
      )
    }
  }.onFailure {
    throw ExecException(it, commandLine, arguments)
  }.getOrThrow()
}

suspend fun exec(commandLine: String, args: List<String?>): ExecOutput =
  exec(commandLine, *args.toTypedArray())
