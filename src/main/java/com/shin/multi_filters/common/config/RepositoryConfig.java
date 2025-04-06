package com.shin.multi_filters.common.config;

import com.shin.multi_filters.common.repository.FilterPageAndSortRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.shin.multi_filters.repository", repositoryBaseClass = FilterPageAndSortRepositoryImpl.class)
public class RepositoryConfig {
}
