package blockman.server.init


import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
import org.springframework.context.annotation.*
import org.springframework.core.io.ClassPathResource
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

import org.springframework.web.multipart.support.StandardServletMultipartResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


import javax.sql.DataSource
import java.io.IOException
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.Properties

@Configuration
//@EnableAspectJAutoProxy
@Lazy
@ComponentScan(value = ["blockman.server.controller"])
open class RootConfig : WebMvcConfigurer {


    override fun addInterceptors(registry: InterceptorRegistry?) {
        //        registry.addInterceptor(getAuthInterceptor()).excludePathPatterns("/getCaptcha/*","/user/register",
        //                "/user/login");
    }

    override fun addCorsMappings(registry: CorsRegistry) {

        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "PUT", "POST", "DELETE", "OPTIONS")
            .allowedHeaders(
                "authorization", "Origin", "No-Cache", "X-Requested-With", "If-Modified-Since",
                "Pragma", "Last-Modified", "Cache-Control", "Expires", "Content-Type", "X-E4M-With"
            )
            .allowCredentials(true)
    }

    @Bean
    open fun multipartResolver(): StandardServletMultipartResolver {
        return StandardServletMultipartResolver()
    }


//@Bean()
//    open fun sqlSessionFactoryBean():SqlSessionFactoryBean{
//
//        val factoryBean = SqlSessionFactoryBean()
//        factoryBean.setDataSource(dataSource())
//        factoryBean.setTypeAliasesPackage("com.voidgeek.cshop.bean")
//        return factoryBean
//    }


    //    @Bean
    //    public static MapperScannerConfigurer getMapperScannerConfigurer()
    //    {
    //        MapperScannerConfigurer mapperScannerConfigurer=new MapperScannerConfigurer();
    //        mapperScannerConfigurer.setBasePackage("com.voidgeek.frbe.mapper");
    //
    //        return mapperScannerConfigurer;
    //    }


    //    @Bean
    //    public MultipartResolver multipartResolver() throws IOException {
    //        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver();
    //        multipartResolver.setDefaultEncoding("UTF-8");
    //        multipartResolver.setMaxUploadSizePerFile(2*1024*1024);
    //        multipartResolver.setMaxInMemorySize(2*1024*1024);
    //        multipartResolver.setPreserveFilename(true);
    //        multipartResolver.setUploadTempDir(new FileSystemResource("fileUpload/temp"));
    //        multipartResolver.setResolveLazily(true);
    //
    //        return multipartResolver;
    //
    //    }


    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {

        val builder = Jackson2ObjectMapperBuilder()
            .indentOutput(true)
            .dateFormat(SimpleDateFormat("yyyy-MM-dd"))

        //将ObjectMapper作为MessageConverter的初始化参数
        //converters.add(MappingJackson2HttpMessageConverter(builder.build<ObjectMapper>()))
    }

    companion object {

//        @Bean
//        fun propertyPlaceholderConfigurer(): PropertyPlaceholderConfigurer {
//            val configurer = PropertyPlaceholderConfigurer()
//            configurer.setLocation(ClassPathResource("app.properties"))
//            return configurer
//        }
    }

}
