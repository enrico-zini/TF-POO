import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class CatalogoAssinatura {
    private List<Assinatura> assinaturas;

    public CatalogoAssinatura() {
        assinaturas = new LinkedList<>();
    }

    public void cadastra(Assinatura a) {
        assinaturas.add(a);
    }

    public void remove(int codigo)
    {
        List<Assinatura> nova = new LinkedList<>();
        for (Assinatura assinatura : assinaturas) {
            if(assinatura.getCodigo()!=codigo)
            {
                nova.add(assinatura);
            }
        }
        assinaturas.clear();
        assinaturas.addAll(nova);
    }

    public Assinatura getProdutoNaLinha(int linha) {
        if (linha >= assinaturas.size()) {
            return null;
        }
        return assinaturas.get(linha);
    }

    public int getQtdade() {
        return assinaturas.size();
    }

    public Stream<Assinatura> getStream() {
        return assinaturas.stream();
    }

    public void loadFromFile() {
        Path appsFilePath = Path.of("assin.dat");
        try (Stream<String> appsStream = Files.lines(appsFilePath)) {
            appsStream.forEach(str -> assinaturas.add(Assinatura.fromLineFile(str)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToFile() {
        Path appsFilePath = Path.of("assin.dat");
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(appsFilePath, StandardCharsets.UTF_8))) {
            for (Assinatura app : assinaturas) {
                writer.println(app.toLineFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
