package blockman.server.init

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@Configuration
@EnableWebMvc
@Import(RootConfig::class)
open class MvcRootConfig {

}