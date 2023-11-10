import javax.swing.table.AbstractTableModel;

public class AppsViewModel extends AbstractTableModel {
    private CatalogoAplicativos aplicativos;
    private String[] colunas = {"Codigo","Nome","Preco","SO"};

    public AppsViewModel(CatalogoAplicativos aplicativos) {
        this.aplicativos = aplicativos;
    }

    public String getColumnName(int col) {
        return colunas[col];
    }

    public int getRowCount() {
        return aplicativos.getQtdade();
    }

    public int getColumnCount() {
        return colunas.length;
    }

    public Object getValueAt(int row, int col) {
        Aplicativo app = aplicativos.getProdutoNaLinha(row);
        switch (col) {
            case 0: return (Object) (app.getCodigo());
            case 1: return (Object) (app.getNome());
            case 2: return (Object) (app.getPreco());
            case 3: return (Object) (app.getSo());
            default: return null;
        }
    }

    public boolean isCellEditable(int row, int col) {
        switch (col) {
            case 0: return false;  
            case 3: return false;      
            default: return true;
        }
    }

    public void setValueAt(Object value, int row, int col) {//coloca o Object value na linha row e coluna col
        Aplicativo aux = aplicativos.getProdutoNaLinha(row);//pega referencia para app da linha
        if (col == 1) {
            aux.setNome((String) value);
        }
        fireTableCellUpdated(row, col);
    }
}