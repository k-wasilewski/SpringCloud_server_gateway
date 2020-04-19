package com.springcloud.server_gateway;

import org.apache.commons.io.FilenameUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
@RestController
public class ServerGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerGatewayApplication.class, args);
    }

    @RequestMapping(path = "/book-service-history", method = RequestMethod.GET)
    public String exportBookServiceHistory() throws IOException {
        return initializeSCDFtask("wrapper-task_db");
    }

    //curl -X POST -F dump=@/home/kuba/Desktop/mydump.txt http://localhost:8084/book-service-dump
    @RequestMapping(path = "/book-service-dump", method = RequestMethod.POST,
            consumes = {"multipart/form-data"})
    public String importQuestion(@RequestParam("dump") MultipartFile multipart) throws IOException {
        if (!FilenameUtils.getExtension(multipart.getOriginalFilename()).equals("sql")) {
            return "Filename extension must be .sql !";
        }
        write(multipart,
                FileSystems.getDefault().getPath(newFolder()));
        return initializeSCDFtask("wrapper-task_db3");
    }

    @RequestMapping(path = "/rating-service-history", method = RequestMethod.GET)
    public String exportRatingServiceHistory() throws IOException {
        return initializeSCDFtask("wrapper-task_db2");
    }

    private String initializeSCDFtask(String taskName) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:9393/tasks/executions");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", taskName));
        httpPost.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpResponse response = client.execute(httpPost);
        return response.toString();
    }

    public String write(MultipartFile file, Path dir) {
        Path filepath = Paths.get(dir.toString(), file.getOriginalFilename());

        try (OutputStream os = Files.newOutputStream(filepath)) {
            os.write(file.getBytes());
        } catch (IOException ioe) {return "IOException at saving file";}
        return filepath+" successfully saved";
    }

    private String newFolder() {
        File file = new File("/home/kuba/Desktop/projects/SpringCloud");
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        List<Integer> ints = new ArrayList<>();
        int i=0;
        for(String str:directories){
            int parsedNo;
            try {
                parsedNo=Integer.parseInt(str.trim());
                ints.add(parsedNo);
            } catch (NumberFormatException nfe) {}
            i++;
        }
        Collections.sort(ints, Collections.reverseOrder());

        int newNumber=0;
        if (!ints.isEmpty()) newNumber = ints.get(0)+1;
        File newFolder = new File("/home/kuba/Desktop/projects/SpringCloud/"+newNumber);
        newFolder.mkdirs();

        return "/home/kuba/Desktop/projects/SpringCloud/"+newNumber;
    }
}
