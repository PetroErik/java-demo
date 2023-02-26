package com.example.demo.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRegistration {
  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Integer id;
  @ManyToOne(optional = false)
  @JoinColumn(name = "student")
  private Student student;
  @ManyToOne(optional = false)
  @JoinColumn(name = "course")
  private Course course;

  public CourseRegistration(Student student, Course course) {
    this.student = student;
    this.course = course;
  }
}
