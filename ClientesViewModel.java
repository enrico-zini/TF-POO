import javax.swing.table.AbstractTableModel;

public class ClientesViewModel extends AbstractTableModel{
    private CatalogoClientes clientes;
    private String[] colunas = {"CPF","Nome","Email"};

    public ClientesViewModel(CatalogoClientes clientes) {
        this.clientes = clientes;
    }

    public String getColumnName(int col) {
        return colunas[col];
    }

    public int getRowCount() {
        return clientes.getQtdade();
    }

    public int getColumnCount() {
        return colunas.length;
    }

    public Object getValueAt(int row, int col) {
        Cliente app = clientes.getProdutoNaLinha(row);
        switch (col) {
            case 0: return (Object) (app.getCpf());
            case 1: return (Object) (app.getNome());
            case 2: return (Object) (app.getEmail());
            default: return null;
        }
    }

    public boolean isCellEditable(int row, int col) {
        switch (col) {
            case 0: return false;        
            default: return true;
        }
    }

    public void setValueAt(Object value, int row, int col) {//coloca o Object value na linha row e coluna col
        Cliente aux = clientes.getProdutoNaLinha(row);//pega referencia para app da linha
        if (col == 1) {
            aux.setNome((String) value);
        }
        fireTableCellUpdated(row, col);
    }
}
