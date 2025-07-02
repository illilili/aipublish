
package aipublish.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
         registry.addMapping("/**") // 모든 경로에 대해
            .allowedOrigins("https://8080-meritending-aipublish1-ay0y00sfvwz.ws-us120.gitpod.io") // 프론트엔드 주소 허용
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
            .allowedHeaders("*") // 모든 헤더 허용
            .allowCredentials(true); // 쿠키/인증 정보 허용
    }
}


