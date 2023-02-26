package com.example.demo;

import com.example.demo.entity.Course;
import com.example.demo.entity.CourseRegistration;
import com.example.demo.entity.Student;
import com.example.demo.request.CourseRequestBody;
import com.example.demo.request.JwtRequestBody;
import com.example.demo.request.StudentRequestBody;
import com.example.demo.response.JwtResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class Helper {
  public HttpHeaders loginForTest(TestRestTemplate restTemplate, int port) {
    String response = restTemplate.postForObject(
        "http://localhost:" + port + "/login",
        new JwtRequestBody("randomuser123", "password"),
        String.class
    );

    HttpHeaders headers = new HttpHeaders();
    try {
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.add("Authorization",
          "Bearer " + getObjectFromResponse(response, JwtResponse.class).getToken());
    } catch (Exception ignored) {}

    return headers;
  }
  public  <T> T getObjectFromResponse(String response, Class<T> valueType) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(response, valueType);
  }

  public Student insertStudent(TestRestTemplate restTemplate, int port, String expectedName, String expectedEmail) throws JsonProcessingException {
    String response = restTemplate.postForObject(
        "http://localhost:" + port + "/student",
        new HttpEntity<>(new StudentRequestBody(expectedName, expectedEmail), loginForTest(restTemplate, port)),
        String.class
    );

    return getObjectFromResponse(response, Student.class);
  }

  public Course insertCourse(TestRestTemplate restTemplate, int port, String courseName, Integer courseCredit) throws JsonProcessingException {
    String response = restTemplate.postForObject(
        "http://localhost:" + port + "/course",
        new HttpEntity<>(new CourseRequestBody(courseName, courseCredit), loginForTest(restTemplate, port)),
        String.class
    );

    return getObjectFromResponse(response, Course.class);
  }

  public CourseRegistration insertCourseRegistration(TestRestTemplate restTemplate, int port, String studentName,
    String studentEmail, String courseName, Integer courseCredit) throws JsonProcessingException {

    Student insertedStudent = insertStudent(restTemplate, port, studentName, studentEmail);
    Course insertedCourse = insertCourse(restTemplate, port, courseName, courseCredit);

    String response = restTemplate.postForObject(
        "http://localhost:" + port + "/register/student/" + insertedStudent.getId() + "/course/"
            + insertedCourse.getId(),
        new HttpEntity<>(loginForTest(restTemplate, port)),
        String.class
    );

    return getObjectFromResponse(response, CourseRegistration.class);
  }
}
