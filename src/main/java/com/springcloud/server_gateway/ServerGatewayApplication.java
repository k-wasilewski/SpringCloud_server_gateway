package com.springcloud.server_gateway;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
@EnableTaskLauncher
@RestController("/task")
@EnableBinding(Source.class)
public class ServerGatewayApplication {
    @Autowired
    //private Sink sink;
    private Source source;

    public static void main(String[] args) {
        SpringApplication.run(ServerGatewayApplication.class, args);
    }

    @RequestMapping(path = "/rabbitmq", method = RequestMethod.GET)
    public void sendRequest() {
        Map<String, String> prop = new HashMap<>();
        prop.put("server.port", "0");

        final TaskLaunchRequest request =
                new TaskLaunchRequest(
                        "maven://com.springcloud:task_db:0.0.1-SNAPSHOT",
                        null,
                        prop,
                        null,
                        "task_db");

        final GenericMessage<TaskLaunchRequest> genericMessage = new GenericMessage<>(request);

        //this.sink.input().send(genericMessage);
        this.source.output().send(genericMessage);
    }

    @RequestMapping(path = "/book-service-history", method = RequestMethod.GET)
    public String initializeSCDFtask() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:9393/tasks/executions");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", "wrapper-task_db"));
        httpPost.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpResponse response = client.execute(httpPost);
        return response.toString();
    }

}
