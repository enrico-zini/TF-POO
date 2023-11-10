public class Cliente {
    private String cpf;
    private String nome;
    private String email;
    
    public Cliente(String cpf, String nome, String email) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
    }
    public String getCpf() {
        return cpf;
    }
    public String getNome() {
        return nome;
    }
    public String getEmail() {
        return email;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String toLineFile(){
        return cpf+","+nome+","+email;
    }
    
    public static Cliente fromLineFile(String line){
        String[] tokens = line.split(",");
        String cpf = tokens[0];
        String nome = tokens[1];
        String email = tokens[2];
        return new Cliente(cpf,nome,email);
    }
    @Override
    public String toString() {
        return "Cliente [cpf=" + cpf + ", nome=" + nome + ", email=" + email + "]";
    }
    
}
