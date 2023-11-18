import javax.swing.table.AbstractTableModel;

public class CobrancasViewModel extends AbstractTableModel {
    private CatalogoCobranca cobrancas;
    private String[] colunas = {"Nome","Email","Preco"};

    public CobrancasViewModel(CatalogoCobranca cobrancas) {
        this.cobrancas = cobrancas;
    }

    public String getColumnName(int col) {
        return colunas[col];
    }

    public int getRowCount() {
        return cobrancas.getQtdade();
    }

    public int getColumnCount() {
        return colunas.length;
    }

    public Object getValueAt(int row, int col) {
        Cobranca app = cobrancas.getProdutoNaLinha(row);
        switch (col) {
            case 0: return (Object) (app.getNome());
            case 1: return (Object) (app.getEmail());
            case 2: return (Object) (app.getValor());
            default: return null;
        }
    }
}
