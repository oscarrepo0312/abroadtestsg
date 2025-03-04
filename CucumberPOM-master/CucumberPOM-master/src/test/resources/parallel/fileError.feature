Feature: Validate error when no join key exists in two flat files

  Scenario: Error should be thrown when the join key is missing
    Given Flat file "data/employees.csv" exists
    And Flat file "data/departments.csv" exists
    When I attempt to join the files on "DeptID"
    Then An error message should be displayed "Join key not found in one or both files"