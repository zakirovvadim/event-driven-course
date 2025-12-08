package ru.vadim.orderservice.config;

import com.thoughtworks.xstream.XStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XStreamConfig {
    @Bean
    XStream xStream() {
        XStream xStream = new XStream();
        xStream.allowTypesByWildcard(new String[] { "ru.vadim.orderservice.**","ru.vadim.common.**" });	 // Change this to your package name
        return xStream;
    }
}
