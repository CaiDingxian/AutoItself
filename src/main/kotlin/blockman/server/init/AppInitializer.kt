package blockman.server.init


import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer

import javax.servlet.MultipartConfigElement
import javax.servlet.ServletContext
import javax.servlet.ServletException
import javax.servlet.ServletRegistration

class AppInitializer : AbstractAnnotationConfigDispatcherServletInitializer() {


    override fun customizeRegistration(registration: ServletRegistration.Dynamic) {

        // Optionally also set maxFileSize, maxRequestSize, fileSizeThreshold

        val multipartConfigElement =
            MultipartConfigElement("/tmp", (2 * 1024 * 1024).toLong(), (2 * 1024 * 1024).toLong(), 2 * 1024 * 1024)

        registration.setMultipartConfig(multipartConfigElement)


    }

    @Throws(ServletException::class)
    override fun onStartup(servletContext: ServletContext) {
        super.onStartup(servletContext)
        //        EnumSet<DispatcherType> enumeration=EnumSet.of(DispatcherType.REQUEST,DispatcherType.INCLUDE,DispatcherType.ERROR,DispatcherType.FORWARD);
        //        servletContext.addFilter("SafeFilter",new SafeFilter())
        //        .addMappingForUrlPatterns(enumeration,true,"/*");

    }

    override fun getRootConfigClasses(): Array<Class<*>>? {


        // BasicConfigurator.configure();
        return arrayOf(MvcRootConfig::class.java)
    }

    override fun getServletConfigClasses(): Array<Class<*>>? {
        return null
    }

    override fun getServletMappings(): Array<String> {
        return arrayOf("/app/*", "/")
    }
}
