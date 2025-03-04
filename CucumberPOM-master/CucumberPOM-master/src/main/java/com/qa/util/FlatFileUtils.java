import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class FlatFileUtils {

    /**
     * Reads a CSV (flat file) and returns a List of Maps where each Map represents a row.
     *
     * @param filePath The path of the flat file.
     * @return List of Map<String, String> (columnName -> columnValue)
     * @throws IOException If the file cannot be read.
     */
    public static List<Map<String, String>> readFlatFile(String filePath) throws IOException {
        List<Map<String, String>> records = new ArrayList<>();
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String headerLine = reader.readLine();
            if (headerLine == null) throw new IOException("Empty file: " + filePath);
            
            String[] headers = headerLine.split(",");
            String line;
            
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, String> record = new LinkedHashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    record.put(headers[i].trim(), values[i].trim());
                }
                records.add(record);
            }
        }
        return records;
    }