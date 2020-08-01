package com.example.ConstructionCompanyApplication

import com.example.ConstructionCompanyApplication.dto.Staff
import org.springframework.hateoas.*
import org.springframework.hateoas.client.Traverson
import org.springframework.hateoas.server.core.TypeReferences
import org.springframework.hateoas.server.core.TypeReferences.CollectionModelType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.net.URISyntaxException


@Controller
class HomeController(private val rest: RestTemplate) {
    @GetMapping(path=["/staff"])
    @Throws(URISyntaxException::class)
    fun index(): String {
        val client =
            Traverson(URI("$REMOTE_SERVICE_ROOT_URI/staff"), MediaTypes.HAL_JSON)
        val employees: CollectionModel<EntityModel<Staff>>? = client.follow()
            .toObject<PagedModel<EntityModel<Staff>>>(object :
                TypeReferences.PagedModelType<EntityModel<Staff>>() {})
        return "index"
    }

//    /**
//     * Instead of putting the creation link from the remote service in the template (a security concern), have a local
//     * route for POST requests. Gather up the information, and form a remote call, using [Traverson] to
//     * fetch the employees [Link]. Once a new employee is created, redirect back to the root URL.
//     *
//     * @param employee
//     * @return
//     * @throws URISyntaxException
//     */
//    @PostMapping("/employees")
//    @Throws(URISyntaxException::class)
//    fun newEmployee(@ModelAttribute employee: Employee?): String {
//        val client =
//            Traverson(URI(REMOTE_SERVICE_ROOT_URI), MediaTypes.HAL_JSON)
//        val employeesLink = client //
//            .follow("employees") //
//            .asLink()
//        rest.postForEntity(employeesLink.expand().href, employee, Employee::class.java)
//        return "redirect:/"
//    }

    companion object {
        private const val REMOTE_SERVICE_ROOT_URI = "http://localhost:8080"
    }

}