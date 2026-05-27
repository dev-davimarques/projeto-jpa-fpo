package reporter;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;
import java.util.List;

public class JSONReporter<T> implements Reporter {

    private final List<T> data;
    private final String filePath;

    public JSONReporter(List<T> data, String filePath) {
        this.data = data;
        this.filePath = filePath;
    }

    @Override
    public void process() {
        // Criação imutável usando o Builder do JsonMapper
        JsonMapper mapper = JsonMapper.builder()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .build();

        try {
            mapper.writeValue(new File(filePath), data);
            System.out.println("Relatório JSON gerado com sucesso em: " + filePath);
        } catch (JacksonException e) {
            System.err.println("Erro interno do Jackson ao gerar JSON: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro de sistema ao gravar o arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}