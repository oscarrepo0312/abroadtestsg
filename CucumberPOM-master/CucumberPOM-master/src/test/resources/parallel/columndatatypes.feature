Feature: Validate column data types after joining two flat files

  Scenario: Validate column data types in the merged dataset
    Given Flat file "data/employees.csv" exists
    And Flat file "data/departments.csv" exists
    When I join the files on "DeptID"
    Then The columns should have the following data types:
      | Column   | DataType  |
      | EmpID    | Integer   |
      | Name     | String    |
      | DeptID   | String    |
      | DeptName | String    |