public class Cobranca {
    private String nome;
    private String email;
    private Double valor;
    public Cobranca(String nome, String email, Double valor) {
        this.nome = nome;
        this.email = email;
        this.valor = valor;
    }
    public String getNome() {
        return nome;
    }
    public String getEmail() {
        return email;
    }
    public Double getValor() {
        return valor;
    }
    public void somaValor(Double v)
    {
        valor+=v;
    }
    public String toLineFile(){
        return nome+","+email+","+valor;
    }

    @Override
    public String toString() {
        return "[nome=" + nome + ", email=" + email + ", valor=" + valor + "]";
    }
    
}
