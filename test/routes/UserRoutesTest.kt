package routes

import com.caiomessias.dao.NewUserDao
import com.caiomessias.dao.UserDao
import com.caiomessias.userRoutesComponent
import com.caiomessias.services.UserService
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.*

class UserRoutesTest {
    @Test
    fun `GET to an existing user should return 200`() {
        val userService = mockk<UserService>()
        val user = UserDao(1L, "test", true)
        coEvery { userService.getById(1L) } returns user

        withTestApplication({ userRoutesComponent(userService) }) {
            with(handleRequest(HttpMethod.Get, "/users/1")) {
                assertEquals(HttpStatusCode.OK, response.status())
                val content = response.content
                assertNotNull(content)
                assertTrue(content.contains("name"))
            }

        }
    }

    @Test
    fun `GET to a non-existing user should return 404`() {
        val userService = mockk<UserService>()
        coEvery { userService.getById(1L) } returns null

        withTestApplication({ userRoutesComponent(userService) }) {
            with(handleRequest(HttpMethod.Get, "/users/1")) {
                assertEquals(HttpStatusCode.NotFound, response.status())
                assertNull(response.content)
            }
        }
    }

    @Test
    fun `POST to user with valid payload should return 201`() {
        val userService = mockk<UserService>()
        coEvery { userService.createUser(NewUserDao("valid", false)) } returns UserDao(1, "valid", false)

        withTestApplication({ userRoutesComponent(userService) }) {
            handleRequest(HttpMethod.Post, "/users") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""{"name":"valid"}""")
            }.apply {
                assertEquals(HttpStatusCode.Created, response.status())
                assertNotNull(response.content)
            }
        }
    }

    @Test
    fun `POST to user with size less than 3 should return 400`() {
        val userService = mockk<UserService>()

        withTestApplication({ userRoutesComponent(userService) }) {
            handleRequest(HttpMethod.Post, "/users") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""{"name":"a"}""")
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                assertEquals("""[{"field":"name","message":"Size must be between 3 and 128"}]""", response.content)
            }
        }
    }
}
