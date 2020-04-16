package com.springcloud.server_gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.deployer.spi.task.TaskLauncher;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.task.launcher.TaskLaunchRequest;
import org.springframework.cloud.task.launcher.annotation.EnableTaskLauncher;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
@EnableTaskLauncher
@RestController
@EnableBinding(Source.class)
public class ServerGatewayApplication {
    @Autowired
    //private Sink sink;
    private Source source;

    public static void main(String[] args) {
        SpringApplication.run(ServerGatewayApplication.class, args);
    }

    @RequestMapping(path = "/task", method = RequestMethod.GET)
    public void sendRequest() {

        final TaskLaunchRequest request =
                new TaskLaunchRequest(
                        "maven://com.springcloud:task_db:jar:0.0.1-SNAPSHOT",
                        null,
                        null,
                        null,
                        "task_db");

        final GenericMessage<TaskLaunchRequest> genericMessage = new GenericMessage<>(request);

        //this.sink.input().send(genericMessage);
        this.source.output().send(genericMessage);
    }

}
