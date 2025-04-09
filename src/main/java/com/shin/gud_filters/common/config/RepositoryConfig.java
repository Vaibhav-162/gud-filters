package com.shin.gud_filters.common.config;

import com.shin.gud_filters.common.repository.GudFiltersRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "${gud-filters.repository.packageName}", repositoryBaseClass = GudFiltersRepositoryImpl.class)
public class RepositoryConfig {
}
