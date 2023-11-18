import java.util.LinkedList;
import java.util.List;

public class CatalogoCobranca {
    private List<Cobranca> cobrancas;

    public CatalogoCobranca() {
        cobrancas = new LinkedList<>();
    }

    public void cadastra(Cobranca c) {
        Cobranca aux = this.contemNome(c.getNome());//busca cobranca com mesmo nome
        if(aux!=null)//se achar
        {
            Double d = c.getValor();
            aux.somaValor(d);//apenas soma valor
        }
        else
        {
            cobrancas.add(c);
        }
    }

    public Cobranca contemNome(String n)
    {
        for (Cobranca cobrar : cobrancas) {
            if(cobrar.getNome().equals(n))
            return cobrar;
        }
        return null;
    }

    public Cobranca getProdutoNaLinha(int linha) {
        if (linha >= cobrancas.size()) {
            return null;
        }
        return cobrancas.get(linha);
    }

    public int getQtdade()
    {
        return cobrancas.size();
    }

    public void remove(String nome)
    {
        List<Cobranca> nova = new LinkedList<>();
        for (Cobranca c : cobrancas) {
            if(c.getNome().equals(nome))
            {
                nova.add(c);
            }
        }
        cobrancas.clear();
        cobrancas.addAll(nova);
    }

    @Override
    public String toString() {
        return cobrancas.toString();
    }

    
}
