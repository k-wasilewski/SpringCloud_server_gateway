package com.springcloud.server_gateway;

import org.springframework.cloud.deployer.spi.core.AppDeploymentRequest;
import org.springframework.cloud.deployer.spi.core.RuntimeEnvironmentInfo;
import org.springframework.cloud.deployer.spi.task.TaskLauncher;
import org.springframework.cloud.deployer.spi.task.TaskStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskSinkConfiguration {
    @Bean
    public TaskLauncher taskLauncher() {
        return new TaskLauncher() {
            @Override
            public String launch(AppDeploymentRequest request) {
                return "task launched";
            }

            @Override
            public void cancel(String id) {

            }

            @Override
            public TaskStatus status(String id) {
                return null;
            }

            @Override
            public void cleanup(String id) {

            }

            @Override
            public void destroy(String appName) {

            }

            @Override
            public RuntimeEnvironmentInfo environmentInfo() {
                return null;
            }
        };
    }
}
