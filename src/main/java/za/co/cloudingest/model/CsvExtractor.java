package za.co.cloudingest.service;

import za.co.cloudingest.model.RawJobRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Extract step: reads a raw job-listings CSV and turns each row into a
 * RawJobRecord (not yet cleaned or validated — that's JobTransformer's job).
 *
 * Expected columns: title,company,location,description,required_skills,salary_range,source_url
 */
public class CsvExtractor {

    public List<RawJobRecord> extract(Reader csvReader) throws IOException {
        List<RawJobRecord> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(csvReader)) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false; // skip header row
                    continue;
                }
                if (line.isBlank()) continue;

                String[] fields = line.split(",");
                if (fields.length < 7) {
                    continue; // skip malformed rows rather than failing the whole batch
                }

                records.add(new RawJobRecord(
                        fields[0], fields[1], fields[2], fields[3],
                        fields[4], fields[5], fields[6]
                ));
            }
        }
        return records;
    }
}

