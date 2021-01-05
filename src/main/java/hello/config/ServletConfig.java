package hello.config;

import hello.servlet.DemoServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by linrx1 on 2020/8/3.
 */
@Configuration
public class ServletConfig {
    @Bean
    public DemoServlet demoServlet(){
        return new DemoServlet();
    }
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(demoServlet(), "/demoServlet/*");
    }
}
