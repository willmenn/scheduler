package com.broker.scheduler.service.v3.analytics;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DumpLogToCsv {


    private List<String> lines;

    public DumpLogToCsv() {
        this.lines = new ArrayList<>();
    }

    public void clearCollection() {
        this.lines = new ArrayList<>();
    }

    public void addLine(int iterationNumber, int lowerScore, int currentScore, double threshold) {
        lines.add(new StringBuilder()
                .append(iterationNumber).append(",")
                .append(lowerScore).append(",")
                .append(currentScore).append(",")
                .append(new Double(threshold).toString())
                .toString());
    }

    public void saveToFile(String scheduleId) {

        try {
            FileWriter writer = new FileWriter(scheduleId + ".csv");
            // headers
            writer.write("iteration,lower_score,current_score,threshold\n");
            writer.flush();

            for (String line : lines) {
                writer.write(line);
                writer.write("\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
