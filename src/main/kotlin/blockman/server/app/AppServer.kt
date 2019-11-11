package blockman.server.app


import org.eclipse.jetty.server.Server

import org.eclipse.jetty.webapp.WebAppContext


fun main() {
    AppServer().runAndScan()
}


class AppServer {

    fun runAndScan() {
        val server = Server(8080)
//http://localhost:8080/brand/getById?id=1
        //Enable parsing of jndi-related parts of web.xml and jetty-env.xml
        val classlist = org.eclipse.jetty.webapp.Configuration.ClassList.setServerDefault(server)
        classlist.addAfter(
            "org.eclipse.jetty.webapp.FragmentConfiguration",
            "org.eclipse.jetty.plus.webapp.EnvConfiguration",
            "org.eclipse.jetty.plus.webapp.PlusConfiguration"
        )
        classlist.addBefore(
            "org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
            "org.eclipse.jetty.annotations.AnnotationConfiguration"
        )

        //Create a WebApp
        val webapp = WebAppContext()
        webapp.contextPath = "/"
        webapp.resourceBase = "E:\\DEV\\Project\\libgdx\\TestModSys\\build\\classes\\kotlin\\main"
        //webapp.war="E:\\DEV\\Project\\libgdx\\TestModSys\\build\\libs\\TestModSys-1.0.war"
//        webapp.war =
//            "E:\\DEV\\Project\\2019\\CShop\\build\\libs\\CShop.war"
        server.handler = webapp

        //Register new transaction manager in JNDI
        //At runtime, the webapp accesses this as java:comp/UserTransaction

        //Define an env entry with webapp scope.
        val maxAmount = org.eclipse.jetty.plus.jndi.EnvEntry(webapp, "maxAmount", 100, true)


//        server.start()
//        server.join()
        server.start()
//        SpringServletContainerInitializer().onStartup(null,handler.servletContext)
//        //ServletContainerInitializersStarter(handler).start()
        server.join()
    }


    @Throws(Exception::class)
    fun run() {
        val webapp = WebAppContext()
        webapp.contextPath = "/"


//        val contexts=ContextHandlerCollection()
//        val server = Server(QueuedThreadPool(5,1))
//
//        server.connectors= arrayOf(ServerConnector(server).apply { port=8080 })
//        server.handler=HandlerCollection().apply {
//            handlers= arrayOf(
//                contexts,DefaultHandler()
//            )
//        }
//
//        val dm=DeploymentManager()
//        dm.contexts=contexts
//        dm.appProviders= listOf(
//            WebAppProvider().apply {
//            isExtractWars=true
//            monitoredResources= listOf(Resource.newResource("E:\\DEV\\Project\\2019\\CShop\\build\\libs\\exploded\\CShop-1.0.war\\"))
//            scanInterval=1
//        })
//        server.beans= listOf(dm)

        //val w = WebAppContext()
        //w.configurations= arrayOf(AnnotationConfiguration())
        //w.contextPath = CONTEXT_PATH
        //w.classLoader = Thread.currentThread().contextClassLoader
        //w.setResourceBase(".")
        //w.setTempDirectory(workDir);
        //w.classLoader = Thread.currentThread().contextClassLoader;

        //w.contextPath="/"

        // w.isParentLoaderPriority=true
        //w.resourceBase= "E:\\DEV\\Project\\2019\\CShop\\build\\libs\\exploded\\CShop-1.0.war\\"
        //print(w.resourceBase)

        //server.setHandler(servletContextHandler(webApplicationContext()));

    }

    companion object {
        private val DEFAULT_PORT = 8080
        private val CONTEXT_PATH = "/"
        private val MAPPING_URL = "/*"
    }

    //    private ServletContextHandler servletContextHandler(WebApplicationContext context) {
    //        ServletContextHandler handler = new ServletContextHandler();
    //        handler.setContextPath(CONTEXT_PATH);
    //        handler.addServlet(new ServletHolder(new DispatcherServlet(context)), MAPPING_URL);
    //        handler.addEventListener(new ContextLoaderListener(context));
    //        return handler;
    //    }

    //    private WebApplicationContext webApplicationContext() {
    //        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
    //        context.register(AppConfiguration.class);
    //        return context;
    //    }
}
