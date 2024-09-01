package org.http4k.intellij.wizard

import org.http4k.client.JavaHttpClient
import org.http4k.core.Body
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
import org.http4k.lens.Header.LOCATION

class ToolboxApi(
    rawHttp: HttpHandler = JavaHttpClient(),
    rootUri: Uri = Uri.of("https://intellij.http4k.org"),
    clientId: ClientId = ClientId.of("intellij_plugin"),
    debug: Boolean
) {
    private val http =
        ClientFilters.HandleRemoteRequestFailed({ status.successful || status.redirection })
            .then(SetHeader("x-http4k-client-id", clientId.value))
            .then(ClientFilters.SetHostFrom(rootUri))
            .then(if(debug) rawHttp.debug() else rawHttp)

    fun questionnaire() =
        Body.auto<Questionnaire>().toLens()(http(Request(GET, "/api/v1/project/questionnaire")))

    fun stackIdFor(answer: Answer): StackId {
        val downloadUrl =
            LOCATION(http.debug()(Request(POST, "/api/v1/project/questionnaire").with(Body.auto<Answer>().toLens() of answer)))
        return StackId.of(downloadUrl.toString().substring(downloadUrl.toString().lastIndexOf('/') + 1))
    }

    fun generateProject(stackId: StackId) = http(Request(GET, "/api/v1/stack/$stackId")).body.stream
}
