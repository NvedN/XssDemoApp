package ru.alfa4.xssdemoapp.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class House {
  private int id;
  private int square;
  private String street;
  private String metro;
  private int floor;
  private int price;
}
