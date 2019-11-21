package blockman.server.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class Entry {

    @RequestMapping("/")
    @ResponseBody
    fun index(@RequestBody body: String): String {
        return "OK"
    }
}