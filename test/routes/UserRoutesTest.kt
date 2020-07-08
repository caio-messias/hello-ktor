package routes

import com.caiomessias.dao.UserDao
import com.caiomessias.main
import com.caiomessias.module
import com.caiomessias.moduleWithDep
import com.caiomessias.repository.UserRepositorySQL
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import com.caiomessias.routes.userRoutes
import com.caiomessias.services.UserService
import io.ktor.application.*
import io.ktor.client.tests.utils.main
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlin.test.*

/*
class ApplicationTest {
    @Test
    fun `GET to an existing user should return 200`() {
        withTestApplication(Application::main) {
            with(handleRequest(HttpMethod.Get, "/users")) {
                assertEquals(HttpStatusCode.OK, response.status())

            }
        }
    }

    @Test
    fun `GET to a non-existing user should return 404`() {
    }

    @Test
    fun `POST to user with size less than 3 should return 400`() {
    }

    @Test
    fun `POST to user with valid payload should return 201`() {
    }

}

 */

class ApplicationTest {
    @Test
    fun testRequests() {
        val userService = mockk<UserService>()
        coEvery { userService.getById(1L) } returns UserDao(1, "abc", true)

        withTestApplication({ moduleWithDep(userService) }) {
            with(handleRequest(HttpMethod.Get, "/users/1")) {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Hello from Ktor Testable sample application", response.content)
            }
        }
    }
}
