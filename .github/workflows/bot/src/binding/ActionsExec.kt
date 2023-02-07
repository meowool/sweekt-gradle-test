@file:Suppress("PackageDirectoryMismatch")
@file:JsModule("@actions/exec")

package actions.exec

import kotlin.js.Promise

external fun getExecOutput(
  commandLine: String,
  args: Array<String> = definedExternally,
  options: ExecOptions = definedExternally,
): Promise<ExecOutput>

external interface ExecOptions {
  var ignoreReturnCode: Boolean?
    get() = definedExternally
    set(value) = definedExternally
}

external interface ExecOutput {
  var exitCode: Number
  var stdout: String
  var stderr: String
}
