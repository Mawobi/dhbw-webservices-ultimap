package de.dhbw.mosbach.webservices.ultimap.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.Collections

@Service
class GraphQLHelper(private val dgsRestTemplate: RestTemplate) {
    private val mapper = ObjectMapper()

    fun <T> request(url: String, request: GraphQLQueryRequest, fieldName: String, returnType: Class<T>): T {
        val responseNode = dgsRestTemplate.exchange(
            url,
            HttpMethod.POST,
            httpEntityWithHeaders(request),
            object : ParameterizedTypeReference<JsonNode>() {}
        ).body["data"][fieldName]!!

        return mapper.convertValue(responseNode, returnType)
    }

    private fun httpEntityWithHeaders(graphQlRequest: GraphQLQueryRequest): HttpEntity<String?> {
        val requestMap = Collections.singletonMap("query", graphQlRequest.serialize())

        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_JSON)
        headers.contentType = MediaType.APPLICATION_JSON

        return HttpEntity(mapper.writeValueAsString(requestMap), headers)
    }
}
