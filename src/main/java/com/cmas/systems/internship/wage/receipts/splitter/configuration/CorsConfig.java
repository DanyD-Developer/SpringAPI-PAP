package com.cmas.systems.internship.wage.receipts.splitter.configuration;

import com.cmas.systems.internship.wage.receipts.splitter.WageReceiptFileSplitterProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

import static java.lang.Boolean.TRUE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.*;

/**
 * @author Nelson Neves ( nelson.neves@cmas-systems.com )
 * @since <next-release>
 */

@Configuration
@RequiredArgsConstructor
public class CorsConfig {

	private static final String X_REQUESTED_WITH = "X-Requested-With";

	private final WageReceiptFileSplitterProperties properties;

	@Bean
	public FilterRegistrationBean corsFilterRegistrationBean() {

		FilterRegistrationBean filter = new FilterRegistrationBean( new CorsFilter( urlBasedCorsConfigurationSource() ) );
		filter.setOrder( Ordered.HIGHEST_PRECEDENCE );
		return filter;
	}

	private UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration( "/**", corsConfiguration() );
		return source;

	}

	private CorsConfiguration corsConfiguration() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins( properties.getAllowedOrigins() );
		config.setAllowedMethods( Arrays.asList( GET.name(), PUT.name(), POST.name(), DELETE.name(), PATCH.name() ) );
		config.setAllowCredentials( TRUE );
		config.setAllowedHeaders( Arrays.asList( AUTHORIZATION, CONTENT_TYPE, X_REQUESTED_WITH ) );
		config.setExposedHeaders( Arrays.asList( HttpHeaders.CONTENT_DISPOSITION ) );
		return config;
	}
}
