import io.cucumber.java.en.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import static org.junit.Assert.*;

public class FileJoinErrorSteps {
    private Exception thrownException;

    @Given("Flat file {string} exists")
    public void flat_file_exists(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
    }

    @When("I attempt to join the files on {string}")
    public void attempt_to_join_files_on_key(String joinKey) {
        try {
            String empFile = "data/employees.csv";
            String deptFile = "data/departments_without_key.csv"; // This file is missing DeptID

            List<Map<String, String>> empData = readCSV(empFile);
            List<Map<String, String>> deptData = readCSV(deptFile);

            // Validate if the join key exists in both files
            if (!empData.isEmpty() && !deptData.isEmpty()) {
                if (!empData.get(0).containsKey(joinKey) || !deptData.get(0).containsKey(joinKey)) {
                    throw new IllegalArgumentException("Join key not found in one or both files");
                }
            }

        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Then("An error message should be displayed {string}")
    public void validate_error_message(String expectedMessage) {
        assertNotNull("No exception was thrown", thrownException);
        assertEquals(expectedMessage, thrownException.getMessage());
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