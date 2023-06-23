package vip.yeee.memo.base.websecurityoauth2.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import vip.yeee.memo.base.websecurityoauth2.constant.AuthConstant;

/**
 * SpringSecurity配置类
 *
 * @author yeeee
 * @since 2022/4/28 16:14
 */
//@Order(Integer.MIN_VALUE)
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityOauthConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
        return new Md5PasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .authorizeRequests()
                    .antMatchers(AuthConstant.BASE_EXCLUDE_PATTERNS)
                    .permitAll()
                    .anyRequest()
                    .authenticated()
        ;
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
//    }

    // AuthenticationManager对象在OAuth2认证服务中要使用，提取放入IOC容器中
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
