package com.example.demo.response;

import com.example.demo.entity.Course;
import com.example.demo.entity.Student;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadAllCoursesResponse {
  private Student student;
  private List<Course> courseList;
}
