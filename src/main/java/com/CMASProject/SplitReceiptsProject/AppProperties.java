package com.CMASProject.SplitReceiptsProject;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private AlfrescoProperties alfrescoProperties;
    private String tempFolder;
    @Data
    public static class AlfrescoProperties{
        private String url;
        private String username;
        private String password;
    }
}
