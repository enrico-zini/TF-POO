public class Assinatura {
    private int codigo;
    private int codigoApp;
    private String cpfCliente;
    private String dataInicio;
    private String dataEncerra;
    private String ativa;

    public Assinatura(int codigo, int codigoApp, String cpfCliente, String dataInicio, String dataEncerra, String ativa) {
        this.codigo = codigo;
        this.codigoApp = codigoApp;
        this.cpfCliente = cpfCliente;
        this.dataInicio = dataInicio;
        this.dataEncerra = dataEncerra;
        this.ativa = ativa;
    }

    public int getCodigo() {
        return codigo;
    }

    public int getCodigoApp() {
        return codigoApp;
    }

    public String getCpfCliente() {
        return cpfCliente;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public String getDataEncerra() {
        return dataEncerra;
    }

    public String isAtiva() {
        return ativa;
    }
    
    public String toLineFile(){
        return codigo+","+codigoApp+","+cpfCliente+","+dataInicio+","+dataEncerra+","+ativa;
    }

    public static Assinatura fromLineFile(String line){
        String[] tokens = line.split(",");
        int codigo = Integer.parseInt(tokens[0]);
        int codigoApp = Integer.parseInt(tokens[1]);
        String cpf = tokens[2];
        String dataInicio = tokens[3]; 
        String dataEncerra = tokens[4]; 
        String isAtiva = tokens[5];
        return new Assinatura(codigo,codigoApp,cpf,dataInicio,dataEncerra,isAtiva);
    }

    @Override
    public String toString() {
        return "Assinatura [codigo=" + codigo + ", codigoApp=" + codigoApp + ", cpfCliente=" + cpfCliente
                + ", dataInicio=" + dataInicio + ", dataEncerra=" + dataEncerra + ", ativa=" + ativa + "]";
    }
}
