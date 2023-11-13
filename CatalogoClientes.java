import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class CatalogoClientes {
    private List<Cliente> clientes;

    public CatalogoClientes() {
        clientes = new LinkedList<>();
    }

    public void cadastra(Cliente p) {
        clientes.add(p);
    }

    public void remove(String cpf)
    {
        List<Cliente> nova = new LinkedList<>();
        for (Cliente Cliente : clientes) {
            if(!Cliente.getCpf().equals(cpf))
            {
                nova.add(Cliente);
            }
        }
        clientes.clear();
        clientes.addAll(nova);
    }

    public Cliente getProdutoNaLinha(int linha) {
        if (linha >= clientes.size()) {
            return null;
        }
        return clientes.get(linha);
    }

    public int getQtdade() {
        return clientes.size();
    }

    public Stream<Cliente> getStream() {
        return clientes.stream();
    }

    public void loadFromFile() {
        Path appsFilePath = Paths.get("clientes.dat");
        try (Stream<String> appsStream = Files.lines(appsFilePath)) {
            appsStream.forEach(str -> clientes.add(Cliente.fromLineFile(str)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToFile() {
        Path appsFilePath = Paths.get("clientes.dat");
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(appsFilePath, StandardCharsets.UTF_8))) {
            for (Cliente app : clientes) {
                writer.println(app.toLineFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
