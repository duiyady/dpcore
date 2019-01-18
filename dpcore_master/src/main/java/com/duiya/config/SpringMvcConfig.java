package com.duiya.config;


import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.duiya.interceptor.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@EnableWebMvc
@Configuration
@EnableScheduling
@ComponentScan(value = "com.duiya")
public class SpringMvcConfig implements WebMvcConfigurer {

    @Override
    /**
     * 使用fastjson
     * 配置消息转换器
     * @param converters
     */
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //1、定义一个convert转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        //2、添加fastjson的配置信息
        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        //驼峰转下划线
        SerializeConfig serializeConfig=new SerializeConfig();
        serializeConfig.propertyNamingStrategy= PropertyNamingStrategy.SnakeCase;
        fastJsonConfig.setSerializeConfig(serializeConfig);
        //序列化格式
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullStringAsEmpty
        );
        // 处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        fastConverter.setFastJsonConfig(fastJsonConfig);

        //处理字符串, 避免直接返回字符串的时候被添加了引号
        StringHttpMessageConverter smc = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        converters.add(smc);
        //4、将convert添加到converters中
        converters.add(fastConverter);
    }

    @Bean
    public RequestInterceptor getRequestInterceptor(){
        return new RequestInterceptor();
    }


    @Override
    /**
     * 添加拦截器
     */
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getRequestInterceptor());
    }


    @Override
    /**
     * 设置不忽略”.”后面的参数
     */
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * 文件上传配置
     */
    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(50*1024*1024);
        multipartResolver.setDefaultEncoding("utf-8");
        multipartResolver.setMaxInMemorySize(50*1024*1024);
        return multipartResolver;
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

}
