import io.cucumber.java.en.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import static org.junit.Assert.*;

public class FileJoinDataTypeSteps {
    private List<Map<String, String>> mergedData = new ArrayList<>();

    @Given("Flat file {string} exists")
    public void flat_file_exists(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
    }

    @When("I join the files on {string}")
    public void join_the_files_on_key(String joinKey) throws IOException {
        String empFile = "data/employees.csv";
        String deptFile = "data/departments.csv";

        List<Map<String, String>> empData = readCSV(empFile);
        List<Map<String, String>> deptData = readCSV(deptFile);

        for (Map<String, String> emp : empData) {
            for (Map<String, String> dept : deptData) {
                if (emp.get(joinKey).equals(dept.get(joinKey))) {
                    Map<String, String> mergedRow = new LinkedHashMap<>();
                    mergedRow.putAll(emp);
                    mergedRow.putAll(dept);
                    mergedData.add(mergedRow);
                }
            }
        }
    }

    @Then("The columns should have the following data types:")
    public void validate_column_data_types(List<Map<String, String>> expectedDataTypes) {
        for (Map<String, String> expectedColumn : expectedDataTypes) {
            String columnName = expectedColumn.get("Column");
            String expectedType = expectedColumn.get("DataType");

            for (Map<String, String> row : mergedData) {
                assertTrue("Column missing: " + columnName, row.containsKey(columnName));
                assertTrue("Incorrect data type for " + columnName, validateDataType(row.get(columnName), expectedType));
            }
        }
    }

    private boolean validateDataType(String value, String expectedType) {
        switch (expectedType.toLowerCase()) {
            case "integer":
                return value.matches("\\d+"); // Numeric check
            case "string":
                return !value.matches("\\d+"); // Should not be purely numeric
            default:
                return false;
        }
    }

    private List<Map<String, String>> readCSV(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        List<Map<String, String>> records = new ArrayList<>();
        String[] headers = reader.readLine().split(",");

        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",");
            Map<String, String> record = new LinkedHashMap<>();
            for (int i = 0; i < headers.length; i++) {
                record.put(headers[i], values[i]);
            }
            records.add(record);
        }
        reader.close();
        return records;
    }
}