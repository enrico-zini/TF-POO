public class Aplicativo {
    public enum SO { Android, IOS }; 
    private int codigo;
    private String nome;
    private String preco;
    private SO so;
    
    public Aplicativo(int codigo, String nome, String preco, Aplicativo.SO so) {
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
        this.so = so;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public void setSo(SO so) {
        this.so = so;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getPreco() {
        return preco;
    }

    public SO getSo() {
        return so;
    }

    public String toLineFile(){
        return codigo+","+nome+","+preco+","+so;
    }

    public static Aplicativo fromLineFile(String line){
        String[] tokens = line.split(",");
        int codigo = Integer.parseInt(tokens[0]);
        String nome = tokens[1];
        String preco = tokens[2];
        Aplicativo.SO so = Aplicativo.SO.valueOf(Aplicativo.SO.class, tokens[3]);
        return new Aplicativo(codigo,nome,preco,so);
    }

    @Override
    public String toString() {
        return "Aplicativo [codigo=" + codigo + ", nome=" + nome + ", preco=" + preco + ", so=" + so + "]";
    }
}
