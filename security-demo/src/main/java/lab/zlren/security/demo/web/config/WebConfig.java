package lab.zlren.security.demo.web.config;

import lab.zlren.security.demo.web.interceptor.TimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by zlren on 17/10/12.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private TimeInterceptor timeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // registry.addInterceptor(timeInterceptor);
    }

    // @Bean
    // public FilterRegistrationBean timeFilter() {
    //
    //     FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
    //     TimeFilter timeFilter = new TimeFilter();
    //     filterRegistrationBean.setFilter(timeFilter);
    //
    //     // 配置在哪些url中起作用
    //     List<String> urlList = new ArrayList<>();
    //     urlList.add("/*");
    //
    //     filterRegistrationBean.setUrlPatterns(urlList);
    //
    //     return filterRegistrationBean;
    // }
}
