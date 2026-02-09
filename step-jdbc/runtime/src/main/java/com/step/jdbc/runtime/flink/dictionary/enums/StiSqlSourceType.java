package com.step.jdbc.runtime.flink.dictionary.enums;

public enum StiSqlSourceType {
  Table, StoredProcedure;
  
  public int getValue() {
     return ordinal();
  }
  
  public static StiSqlSourceType forValue(int value) {
   return values()[value];
  }
}