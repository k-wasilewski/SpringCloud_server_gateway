package com.springcloud.server_gateway;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletResponse httpServletResponse, HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            System.out.println("status code at MyErrorController: " + status);
        }

        httpServletResponse.setHeader("Location", "http://localhost:8084/login");
        httpServletResponse.setStatus(403);
        return "redirect:http://localhost:8084/login";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}