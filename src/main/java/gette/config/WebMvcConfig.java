package gette.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // UTF-8 필터 등록
    @Bean
    public FilterRegistrationBean<CharacterEncodingFilter> characterEncodingFilterRegistrationBean() {
        FilterRegistrationBean<CharacterEncodingFilter> registrationBean = new FilterRegistrationBean<>();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        registrationBean.setFilter(characterEncodingFilter);
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registrationBean.setName("encodingFilter");
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

}
