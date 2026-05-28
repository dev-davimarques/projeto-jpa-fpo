package reporter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class CSVReporter<T> implements Reporter{

    private final List<T> data;
    private final String filePath;
    private final String[] headers;
    private final Function<T, Object[]> rowMapper;

    public CSVReporter(List<T> data, String filePath, String[] headers, Function<T, Object[]> rowMapper) {
        this.data = data;
        this.filePath = filePath;
        this.headers = headers;
        this.rowMapper = rowMapper;
    }

    @Override
    public void process() {
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader(headers)
                .build();

        try (FileWriter out = new FileWriter(filePath);
             CSVPrinter printer = new CSVPrinter(out, format)) {

            for (T item : data) {
                printer.printRecord(rowMapper.apply(item));
            }

            System.out.println("Relatório CSV gerado com sucesso em: " + filePath);
        } catch (IOException e) {
            System.err.println("Erro ao gerar relatório CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
