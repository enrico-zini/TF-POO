import javax.swing.table.AbstractTableModel;

public class AssinaturasViewModel extends AbstractTableModel{
    private CatalogoAssinatura assinaturas;
    private String[] colunas = {"Codigo","CodigoApp","CPF","DataInicio","DataEncerra","ATIVO"};

    public AssinaturasViewModel(CatalogoAssinatura assinaturas) {
        this.assinaturas = assinaturas;
    }

    public String getColumnName(int col) {
        return colunas[col];
    }

    public int getRowCount() {
        return assinaturas.getQtdade();
    }

    public int getColumnCount() {
        return colunas.length;
    }

    public Object getValueAt(int row, int col) {
        Assinatura app = assinaturas.getProdutoNaLinha(row);
        switch (col) {
            case 0: return (Object) (app.getCodigo());
            case 1: return (Object) (app.getCodigoApp());
            case 2: return (Object) (app.getCpfCliente());
            case 3: return (Object) (app.getDataInicio());
            case 4: return (Object) (app.getDataEncerra());
            case 5: return (Object) (app.isAtiva());
            default: return null;
        }
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }
}
