package edu.csf.persistence.base;
import org.apache.commons.csv.*;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;
import java.io.IOException;


public abstract class
AbstractCsvFileDAO<I, E extends IdentityInterface<I>> implements DAOInterface<I, E> {
    private final CSVPrinter csvPrinter;
    private final String filename;

    private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT.builder().build();

    private boolean isFirstPut = true;

    protected AbstractCsvFileDAO(String className) throws IOException {
        Files.createDirectories(Path.of("databases"));
        this.filename = "databases/" + className + ".csv";
        this.csvPrinter = new CSVPrinter(new FileWriter(filename, true), CSV_FORMAT);
    }

    protected void putInCsvInternal(@NotNull I identity, Object... params) throws IOException {
        if (isFirstPut) {
            new PrintWriter(filename).close();
            isFirstPut = false;
        }
        if (identity instanceof TwoLong ids) {
            this.csvPrinter.printRecord(Stream.concat(Stream.of(ids.first), Stream.of(ids.second)));
        } else {
            this.csvPrinter.printRecord(Stream.concat(Stream.of(identity), Stream.of(params)));
        }
        this.csvPrinter.flush();
    }

    protected Object[] getFromCsvInternal(@NotNull I identity) throws IOException {
        try (var parser = new CSVParser(new BufferedReader(new FileReader(this.filename)), CSV_FORMAT)) {
            if (identity instanceof TwoLong ids) {
                if (ids.first == null) {
                    return parser.getRecords().stream().filter(r -> Objects.equals(r.get(1), Long.toString(ids.second))).toArray();
                } else if (ids.second == null) {
                    return parser.getRecords().stream().filter(r -> Objects.equals(r.get(0), Long.toString(ids.first))).toArray();
                } else {
                    return parser.getRecords().stream().filter(r -> Objects.equals(r.get(0), Long.toString(ids.first))).filter(r -> Objects.equals(r.get(1), Long.toString(ids.second))).toArray();
                }
            } else {
                return parser.getRecords().stream().filter(r -> Objects.equals(r.get(0), Long.toString((Long) identity))).toArray();
            }
        }
    }
}