Feature: Validate data after joining two flat files

  Scenario: Validate the merged data after joining two flat files
    Given Flat file "data/employees.csv" exists
    And Flat file "data/departments.csv" exists
    When I join the files on "DeptID"
    Then The merged dataset should contain the following records:
      | EmpID | Name  | DeptID | DeptName   |
      | 101   | Toshib  | D01    | HR         |
      | 102   | JB | D02    | IT         |
      | 103   | AD   | D01    | HR         |