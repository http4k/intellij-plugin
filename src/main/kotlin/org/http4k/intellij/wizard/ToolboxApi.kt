package org.http4k.intellij.wizard

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success
import org.http4k.client.JavaHttpClient
import org.http4k.cloudnative.RemoteRequestFailed
import org.http4k.core.Body
import org.http4k.core.ContentType.Companion.OCTET_STREAM
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.filter.ClientFilters
import org.http4k.filter.HandleRemoteRequestFailed
import org.http4k.filter.RequestFilters.SetHeader
import org.http4k.filter.debug
import org.http4k.format.Jackson.auto
import org.http4k.lens.FormField
import org.http4k.lens.Header.LOCATION
import org.http4k.lens.MultipartForm
import org.http4k.lens.MultipartFormField
import org.http4k.lens.MultipartFormFile
import org.http4k.lens.Validator.Strict
import org.http4k.lens.WebForm
import org.http4k.lens.enum
import org.http4k.lens.multipartForm
import org.http4k.lens.webForm
import java.io.InputStream

class ToolboxApi(
    rawHttp: HttpHandler = JavaHttpClient(),
    rootUri: Uri = Uri.of("https://intellij.http4k.org"),
    clientId: ClientId = ClientId.of("intellij_plugin"),
    debug: Boolean = false
) {
    private val http =
        ClientFilters.HandleRemoteRequestFailed({ status.successful || status.redirection })
            .then(SetHeader("x-http4k-client-id", clientId.value))
            .then(ClientFilters.SetHostFrom(rootUri))
            .then(if (debug) rawHttp.debug() else rawHttp)

    fun questionnaire() =
        http(Request(GET, "/api/v1/project/questionnaire")).run {
            when {
                status.successful -> Success(Body.auto<Questionnaire>().toLens()(this))
                else -> Failure(RemoteRequestFailed(status, bodyString()))
            }
        }

    fun stackIdFor(answer: Answer) =
        http(Request(POST, "/api/v1/project/questionnaire").with(Body.auto<Answer>().toLens() of answer))
            .run {
                when {
                    status.redirection -> {
                        val downloadUrl = LOCATION(this).toString()
                        Success(StackId.of(downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1)))
                    }

                    else -> Failure(RemoteRequestFailed(status, bodyString()))
                }
            }

    fun generateProject(stackId: StackId) =
        http(Request(GET, "/api/v1/stack/$stackId")).run {
            when {
                status.successful -> Success(body.stream)
                else -> Failure(RemoteRequestFailed(status, bodyString()))
            }
        }

    fun generateMessage(content: InputStream) =
        http(Request(POST, "/api/v1/message").body(content)).run {
            when {
                status.successful -> Success(body.stream)
                else -> Failure(RemoteRequestFailed(status, bodyString()))
            }
        }

    fun generateOpenApiClasses(clientApiStyle: ClientApiStyle , inputStream: InputStream): Result<InputStream, RemoteRequestFailed> {
        val style = MultipartFormField.required("clientApiStyle")
        val field = MultipartFormField.required("packageName")
        val file = MultipartFormFile.required("specification")
        val form = Body.multipartForm(Strict, field, file, style).toLens()

        return http(
            Request(POST, "/api/v1/openapi/file")
                .with(
                    form of MultipartForm()
                        .with(
                            field of MultipartFormField("com.example"),
                            style of MultipartFormField(clientApiStyle.name),
                            file of MultipartFormFile("", OCTET_STREAM, inputStream)
                        )
                )
        ).run {
            when {
                status.successful -> Success(body.stream)
                else -> Failure(RemoteRequestFailed(status, bodyString()))
            }
        }
    }

    fun generateDataClasses(generatorFormat: GeneratorFormat, inputStream: InputStream): Result<InputStream, RemoteRequestFailed> {
        val formatLens = FormField.enum<GeneratorFormat>().required("format")
        val inputLens = FormField.required("input")
        val packageNameLens = FormField.required("packageName")
        val formLens = Body.webForm(Strict, inputLens, formatLens, packageNameLens).toLens()

        val readText = inputStream.reader().readText()
        return http(
            Request(POST, "/api/v1/dataclass/file")
                .with(
                    formLens of WebForm()
                        .with(
                            packageNameLens of "example",
                            formatLens of generatorFormat,
                            inputLens of readText
                        )
                )
        ).run {
            when {
                status.successful -> Success(body.stream)
                else -> Failure(RemoteRequestFailed(status, bodyString()))
            }
        }
    }

    fun generateData4kClasses(generatorFormat: GeneratorFormat, inputStream: InputStream): Result<InputStream, RemoteRequestFailed> {
        val formatLens = FormField.enum<GeneratorFormat>().required("format")
        val inputLens = FormField.required("input")
        val packageNameLens = FormField.required("packageName")
        val formLens = Body.webForm(Strict, inputLens, formatLens, packageNameLens).toLens()

        val readText = inputStream.reader().readText()
        return http(
            Request(POST, "/api/v1/data4k/file")
                .with(
                    formLens of WebForm()
                        .with(
                            packageNameLens of "example",
                            formatLens of generatorFormat,
                            inputLens of readText
                        )
                )
        ).run {
            when {
                status.successful -> Success(body.stream)
                else -> Failure(RemoteRequestFailed(status, bodyString()))
            }
        }
    }
}

enum class GeneratorFormat {
    json, yaml
}

enum class ClientApiStyle {
    standard, connect
}
