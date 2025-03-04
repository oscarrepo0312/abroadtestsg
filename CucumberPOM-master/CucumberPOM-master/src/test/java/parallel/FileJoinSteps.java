import io.cucumber.java.en.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class FileJoinSteps {
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

    @Then("The merged dataset should contain the following records:")
    public void validate_merged_data(List<Map<String, String>> expectedData) {
        assertTrue(mergedData.containsAll(expectedData));
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
