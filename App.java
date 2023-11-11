import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class App {
    private CatalogoAplicativos catApps;
    private CatalogoClientes catClientes;
    private CatalogoAssinatura catAssin;

    private AppsViewModel catAppsVM;
    private ClientesViewModel catClienteVM;
    private AssinaturasViewModel catAssinVM;

    private JTextField tfCodigo;
    private JTextField tfNome;
    private JTextField tfPreco;
    private JComboBox<Aplicativo.SO> cbSo;

    private JTextField tfCpf;
    private JTextField tfNomeCliente;
    private JTextField tfEmail;

    private JTextField tfCodigoAssin;
    private JComboBox<Integer> cbCodigoApp;
    private JComboBox<String> cbCpfCliente;
    private JTextField tfDataInicio;
    private JTextField tfDataEncerra;
    private JComboBox<String> cbStatus;

    private JButton btAdd;

    private Container contentPane1;
    private Container contentPane2;
    private Container contentPane3;

    private JFrame frame;

    public App() {
        catApps = new CatalogoAplicativos();
        catClientes = new CatalogoClientes();
        catAssin = new CatalogoAssinatura();

        catApps.loadFromFile();
        catClientes.loadFromFile();
        catAssin.loadFromFile();
    }

    public void criaJanela() throws Exception {
        frame = new JFrame("Gestão de aplicativos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuItem salvar = new JMenuItem("Salvar");
        salvar.addActionListener(e -> catApps.saveToFile());
        salvar.addActionListener(e -> catClientes.saveToFile());
        salvar.addActionListener(e -> catAssin.saveToFile());
        JMenu salvo = new JMenu("Salvar");
        salvo.add(salvar);

        JMenuItem tabelaApp = new JMenuItem("Tabela Apps");
        tabelaApp.addActionListener(e -> toApps());
        JMenuItem tabelaClient = new JMenuItem("Tabela Clientes");
        tabelaClient.addActionListener(e -> toClients());
        JMenuItem tabelaAssin = new JMenuItem("Tabela Assinaturas");
        tabelaAssin.addActionListener(e -> toAssinaturas());
        JMenu tabelas = new JMenu("Tabelas");
        tabelas.add(tabelaApp);
        tabelas.add(tabelaClient);
        tabelas.add(tabelaAssin);

        JMenuBar menu = new JMenuBar();
        menu.add(salvo);
        menu.add(tabelas);

        contentPane1 = new Container();
        contentPane1.setName("tabelaApps");
        contentPane1.setLayout(new FlowLayout(FlowLayout.LEADING));
        contentPane1.add(criaJanela1());

        contentPane2 = new Container();
        contentPane2.setName("tabelaClientes");
        contentPane2.setLayout(new FlowLayout(FlowLayout.LEADING));
        contentPane2.add(criaJanela2());

        contentPane3 = new Container();
        contentPane3.setName("tabelaAssinaturas");
        contentPane3.setLayout(new FlowLayout(FlowLayout.LEADING));
        contentPane3.add(criaJanela3());

        frame.setJMenuBar(menu);
        frame.setContentPane(contentPane1);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void toClients() {
        frame.setContentPane(contentPane2);
        frame.revalidate();
        frame.repaint();
    }

    public void toApps() {
        frame.setContentPane(contentPane1);
        frame.revalidate();
        frame.repaint();
    }

    public void toAssinaturas() {
        frame.setContentPane(contentPane3);
        frame.revalidate();
        frame.repaint();
    }

    public Container criaJanela1() {
        catAppsVM = new AppsViewModel(catApps);
        JTable tabela = new JTable(catAppsVM);
        tabela.setFillsViewportHeight(true);

        // https://gist.github.com/nis4273/c01c4e339b557f965797
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = tabela.rowAtPoint(evt.getPoint());
                int col = tabela.columnAtPoint(evt.getPoint());
                if (row >= 0 && col == 0) {// quando clicar em algum codigo
                    int codigoApp = (Integer) tabela.getValueAt(row, col);// pega o codigo
                    List<String> listCPF = new LinkedList<>();
                    // ve nas assinaturas e pega apenas as que possuem o codigo e poe o cpf em uma lista
                    catAssin.getStream().filter(a -> a.getCodigoApp() == codigoApp).forEach(a -> listCPF.add(a.getCpfCliente()));
                    CatalogoClientes catClientesRel = new CatalogoClientes();
                    for (String cpf : listCPF) {// cria um novo catalogo so com os clietes dos cpfs
                        catClientes.getStream().filter(c -> c.getCpf().equals(cpf)).forEach(a -> catClientesRel.cadastra(a));
                    }
                    ClientesViewModel aux = new ClientesViewModel(catClientesRel);
                    JTable aux2 = new JTable(aux);
                    JScrollPane sp = new JScrollPane(aux2);
                    JOptionPane.showMessageDialog(null, sp);
                }
            }
        });

        JPanel tabelaApps = new JPanel();
        tabelaApps.setLayout(new BoxLayout(tabelaApps, BoxLayout.PAGE_AXIS));
        JScrollPane scrollPane = new JScrollPane(tabela, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        tabelaApps.add(scrollPane);
        tabelaApps.setName("tabelaApps");
        JPanel novoApp = criaPainelNovoApp();
        tabelaApps.add(novoApp);

        return tabelaApps;
    }

    public JPanel criaJanela2() throws Exception {
        catClienteVM = new ClientesViewModel(catClientes);
        JTable tabela = new JTable(catClienteVM);
        tabela.setFillsViewportHeight(true);

        // https://gist.github.com/nis4273/c01c4e339b557f965797
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = tabela.rowAtPoint(evt.getPoint());
                int col = tabela.columnAtPoint(evt.getPoint());
                if (row >= 0 && col == 0) {// quando clicar em algum cpf
                    String cpf = (String) tabela.getValueAt(row, col);
                    CatalogoAssinatura catAssinR = new CatalogoAssinatura();
                    catAssin.getStream().filter(a -> a.getCpfCliente().equals(cpf)).forEach(a -> catAssinR.cadastra(a));
                    AssinaturasViewModel aux = new AssinaturasViewModel(catAssinR);
                    JTable aux2 = new JTable(aux);
                    JScrollPane sp = new JScrollPane(aux2);
                    JOptionPane.showMessageDialog(null, sp);
                }
            }
        });

        JPanel tabelaClientes = new JPanel(new FlowLayout(FlowLayout.LEADING));
        tabelaClientes.setLayout(new BoxLayout(tabelaClientes, BoxLayout.PAGE_AXIS));
        JScrollPane scrollPane = new JScrollPane(tabela, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        tabelaClientes.add(scrollPane);

        JPanel novoCliente = criaPainelNovoCliente();
        tabelaClientes.add(novoCliente);
        tabelaClientes.setName("tabelaClientes");

        return tabelaClientes;
    }

    public JPanel criaJanela3() throws Exception {
        catAssinVM = new AssinaturasViewModel(catAssin);
        JTable tabela = new JTable(catAssinVM);
        tabela.setFillsViewportHeight(true);

        JPanel tabelaAssin = new JPanel(new FlowLayout(FlowLayout.LEADING));
        tabelaAssin.setLayout(new BoxLayout(tabelaAssin, BoxLayout.PAGE_AXIS));
        JScrollPane scrollPane = new JScrollPane(tabela, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        tabelaAssin.add(scrollPane);

        JPanel novaAssin = criaPainelNovaAssin();
        tabelaAssin.add(novaAssin);
        tabelaAssin.setName("tabelaAssinatura");

        return tabelaAssin;
    }

    public JPanel criaPainelNovoApp() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.PAGE_AXIS));

        JPanel linha1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
        linha1.add(new JLabel("Codigo"));
        tfCodigo = new JTextField(10);
        linha1.add(tfCodigo);
        linha1.add(new JLabel("Nome"));
        tfNome = new JTextField(20);
        linha1.add(tfNome);
        JPanel linha2 = new JPanel(new FlowLayout(FlowLayout.LEADING));
        linha2.add(new JLabel("Preco"));
        tfPreco = new JTextField(10);
        linha2.add(tfPreco);
        linha2.add(new JLabel("Sist. Oper."));
        cbSo = new JComboBox<>(Aplicativo.SO.values());
        linha2.add(cbSo);
        btAdd = new JButton("Novo App");
        btAdd.addActionListener(e -> adicionaApp());
        linha2.add(btAdd);
        JPanel linha3 = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JTextField tf = new JTextField("Codigo");
        JButton delete = new JButton("Remove");
        delete.addActionListener(e -> cancelaApp(Integer.valueOf(tf.getText())));
        linha3.add(delete);
        linha3.add(tf);

        painel.add(linha1);
        painel.add(linha2);
        painel.add(linha3);
        return painel;
    }

    public JPanel criaPainelNovoCliente() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.PAGE_AXIS));

        JPanel linha1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
        linha1.add(new JLabel("CPF"));
        tfCpf = new JTextField(10);
        linha1.add(tfCpf);
        linha1.add(new JLabel("Nome"));
        tfNomeCliente = new JTextField(20);
        linha1.add(tfNomeCliente);
        JPanel linha2 = new JPanel(new FlowLayout(FlowLayout.LEADING));
        linha2.add(new JLabel("Email"));
        tfEmail = new JTextField(10);
        linha2.add(tfEmail);
        btAdd = new JButton("Novo Cliente");
        btAdd.addActionListener(e -> adicionaCliente());
        linha2.add(btAdd);
        JPanel linha3 = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JTextField tf = new JTextField("CPF");
        JButton delete = new JButton("Remove");
        delete.addActionListener(e -> cancelaCliente(tf.getText()));
        linha3.add(delete);
        linha3.add(tf);

        painel.add(linha1);
        painel.add(linha2);
        painel.add(linha3);
        return painel;
    }

    public JPanel criaPainelNovaAssin() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.PAGE_AXIS));

        JPanel linha1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
        linha1.add(new JLabel("Codigo"));

        tfCodigoAssin = new JTextField(10);
        linha1.add(tfCodigoAssin);

        linha1.add(new JLabel("Codigo do App"));
        cbCodigoApp = new JComboBox<>();
        catApps.getStream().forEach(a -> cbCodigoApp.addItem(a.getCodigo()));
        linha1.add(cbCodigoApp);

        linha1.add(new JLabel("CPF"));
        cbCpfCliente = new JComboBox<>();
        catClientes.getStream().forEach(a -> cbCpfCliente.addItem(a.getCpf()));
        linha1.add(cbCpfCliente);

        JPanel linha2 = new JPanel(new FlowLayout(FlowLayout.LEADING));
        linha2.add(new JLabel("Data Inicio"));
        tfDataInicio = new JTextField(5);
        linha2.add(tfDataInicio);

        linha2.add(new JLabel("Data Encerramento"));
        tfDataEncerra = new JTextField(5);
        linha2.add(tfDataEncerra);

        cbStatus = new JComboBox<>();
        cbStatus.addItem("Ativo");
        cbStatus.addItem("Inativo");
        linha2.add(cbStatus);

        JPanel linha3 = new JPanel(new FlowLayout(FlowLayout.LEADING));

        JTextField tf = new JTextField("Codigo");
        JButton delete = new JButton("Remove");
        delete.addActionListener(e -> cancelaAssinatura(Integer.valueOf(tf.getText())));

        btAdd = new JButton("Nova Assinatura");
        btAdd.addActionListener(e -> adicionaAssinatura());

        linha3.add(btAdd);
        linha3.add(delete);
        linha3.add(tf);

        painel.add(linha1);
        painel.add(linha2);
        painel.add(linha3);
        return painel;
    }

    public void adicionaApp() {
        int codigo = Integer.parseInt(tfCodigo.getText());
        String nome = tfNome.getText();
        String preco = tfPreco.getText();
        Aplicativo.SO so = (Aplicativo.SO) cbSo.getSelectedItem();
        Aplicativo novo = new Aplicativo(codigo, nome, preco, so);
        catApps.cadastra(novo);
        catAppsVM.fireTableDataChanged();
        cbCodigoApp.addItem(codigo);
    }

    public void adicionaCliente() {
        String cpf = tfCpf.getText();
        String nome = tfNomeCliente.getText();
        String email = tfEmail.getText();
        Cliente novo = new Cliente(cpf, nome, email);
        catClientes.cadastra(novo);
        catClienteVM.fireTableDataChanged();
        cbCpfCliente.addItem(cpf);
    }

    public void adicionaAssinatura() {
        int codigo = Integer.parseInt(tfCodigoAssin.getText());
        int codigoApp = (Integer) cbCodigoApp.getSelectedItem();
        String cpf = cbCpfCliente.getSelectedItem().toString();
        String dataInicio = tfDataInicio.getText();
        String dataEncerra = tfDataEncerra.getText();
        String status = cbStatus.getSelectedItem().toString();

        Assinatura novo = new Assinatura(codigo, codigoApp, cpf, dataInicio, dataEncerra, status);
        catAssin.cadastra(novo);
        catAssinVM.fireTableDataChanged();
    }

    public void cancelaApp(int codigo) {
        catApps.remove(codigo);
        catAppsVM.fireTableDataChanged();
    }

    public void cancelaCliente(String cpf) {
        catClientes.remove(cpf);
        catClienteVM.fireTableDataChanged();
    }

    public void cancelaAssinatura(int codigo) {
        catAssin.remove(codigo);
        catAssinVM.fireTableDataChanged();
    }

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.criaJanela();
    }
}
