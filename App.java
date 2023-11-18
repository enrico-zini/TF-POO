import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Calendar;
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

    private int countApp = 0;
    private JTextField tfNome;
    private JTextField tfPreco;
    private JComboBox<Aplicativo.SO> cbSo;

    private JTextField tfCpf;
    private JTextField tfNomeCliente;
    private JTextField tfEmail;

    private int countAssin = 0;
    private JComboBox<Integer> cbCodigoApp;
    private JComboBox<String> cbCpfCliente;

    private JButton btAdd;

    private Container contentPane1;
    private Container contentPane2;
    private Container contentPane3;

    private JMenuBar menu;

    private JFrame frame;
    
    private int dia;
    private int mes;
    private int ano;
    
    public App() {
        catApps = new CatalogoAplicativos();
        catClientes = new CatalogoClientes();
        catAssin = new CatalogoAssinatura();

        catApps.loadFromFile();
        catClientes.loadFromFile();
        catAssin.loadFromFile();

        Calendar data = Calendar.getInstance();
        mes = data.get(Calendar.MONTH) + 1;
        ano = data.get(Calendar.YEAR);
        dia = data.get(Calendar.DAY_OF_MONTH);

        menu = criaMenu();

        // atribui valor correto para count para poder auto-encrementar
        catApps.getStream().forEach(ap -> {
            if (ap.getCodigo() > countApp) {
                countApp = ap.getCodigo();
            }
        });
        countApp++;

        catAssin.getStream().forEach(a -> {
            if (a.getCodigo() > countAssin) {
                countAssin = a.getCodigo();
            }
        });
        countAssin++;

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
    }

    public void criaJanela() throws Exception {
        frame = new JFrame("Gestão de aplicativos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setJMenuBar(menu);
        frame.setContentPane(contentPane1);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public JMenuBar criaMenu() {
        JMenuItem salvar = new JMenuItem("Salvar");
        salvar.addActionListener(e -> catApps.saveToFile());
        salvar.addActionListener(e -> catClientes.saveToFile());
        salvar.addActionListener(e -> catAssin.saveToFile());
        JMenu salvo = new JMenu("Arquivo");
        salvo.add(salvar);

        JMenuItem tabelaApp = new JMenuItem("Tabela Apps");
        tabelaApp.addActionListener(e -> toApps());
        JMenuItem tabelaClient = new JMenuItem("Tabela Clientes");
        tabelaClient.addActionListener(e -> toClients());
        JMenuItem tabelaAssin = new JMenuItem("Tabela Assinaturas");
        tabelaAssin.addActionListener(e -> toAssinaturas());
        JMenuItem cobrancas = new JMenuItem("Cobrancas");
        cobrancas.addActionListener(e -> cobrar());
        JMenu tabelas = new JMenu("Tabelas");
        tabelas.add(tabelaApp);
        tabelas.add(tabelaClient);
        tabelas.add(tabelaAssin);
        tabelas.add(cobrancas);

        JMenuItem rendimentosTotais = new JMenuItem("Rendimentos Totais");
        rendimentosTotais.addActionListener(e -> rendimentosTotais());
        JMenuItem rendimentosApps = new JMenuItem("Rendimentos por App");
        rendimentosApps.addActionListener(e -> rendimentoPorApp());
        JMenu rendimentos = new JMenu("Rendimentos");
        rendimentos.add(rendimentosTotais);
        rendimentos.add(rendimentosApps);

        JMenuBar menu = new JMenuBar();
        menu.add(salvo);
        menu.add(tabelas);
        menu.add(rendimentos);

        return menu;
    }

    public void toApps() {
        frame.setContentPane(contentPane1);
        frame.revalidate();
        frame.repaint();
    }
    
    public void toClients() {
        frame.setContentPane(contentPane2);
        frame.revalidate();
        frame.repaint();
    }

    public void toAssinaturas() {
        frame.setContentPane(contentPane3);
        frame.revalidate();
        frame.repaint();
    }

    public JPanel criaJanela1() {
        catAppsVM = new AppsViewModel(catApps);
        JTable tabela = new JTable(catAppsVM);
        tabela.setFillsViewportHeight(true);

        // https://gist.github.com/nis4273/c01c4e339b557f965797
        // mostra clientes relacionados ao app
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = tabela.rowAtPoint(evt.getPoint());
                int col = tabela.columnAtPoint(evt.getPoint());
                if (row >= 0 && col == 0) {// quando clicar em algum codigo
                    int codigoApp = (Integer) tabela.getValueAt(row, col);// pega o codigo
                    CatalogoClientes catClientesRel = new CatalogoClientes();
                    //para cada assinatura ativa com o codigo do aplicativo
                    catAssin.getStream().filter(a -> a.isAtiva().equals("Ativo"))
                            .filter(a -> a.getCodigoApp() == codigoApp).forEach(a -> {
                                // pega o cliente relacionado ao cpf e cadastra em nova tabela
                                catClientes.getStream().filter(c -> c.getCpf().equals(a.getCpfCliente()))
                                        .forEach(c -> catClientesRel.cadastra(c));
                            });
                    ClientesViewModel aux = new ClientesViewModel(catClientesRel);
                    JTable aux2 = new JTable(aux);
                    JScrollPane sp = new JScrollPane(aux2);
                    JOptionPane.showMessageDialog(null, sp, "Clientes", JOptionPane.PLAIN_MESSAGE);
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

    public JPanel criaJanela2() {
        catClienteVM = new ClientesViewModel(catClientes);
        JTable tabela = new JTable(catClienteVM);
        tabela.setFillsViewportHeight(true);

        // https://gist.github.com/nis4273/c01c4e339b557f965797
        // mostra assinaturas relacionadas ao cliente
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = tabela.rowAtPoint(evt.getPoint());
                int col = tabela.columnAtPoint(evt.getPoint());
                if (row >= 0 && col == 0) {// quando clicar em algum cpf
                    String cpf = (String) tabela.getValueAt(row, col);
                    CatalogoAssinatura catAssinR = new CatalogoAssinatura();
                    catAssin.getStream().filter(a -> a.getCpfCliente().equals(cpf))
                            .filter(a -> a.isAtiva().equals("Ativo"))
                            .forEach(a -> catAssinR.cadastra(a));
                    AssinaturasViewModel aux = new AssinaturasViewModel(catAssinR);
                    JTable aux2 = new JTable(aux);
                    JScrollPane sp = new JScrollPane(aux2);
                    JOptionPane.showMessageDialog(null, sp, "Assinaturas", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        JPanel tabelaClientes = new JPanel(new FlowLayout(FlowLayout.LEADING));
        tabelaClientes.setLayout(new BoxLayout(tabelaClientes, BoxLayout.PAGE_AXIS));
        JScrollPane scrollPane = new JScrollPane(tabela, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        tabelaClientes.add(scrollPane);

        JPanel novoCliente = criaPainelNovoCliente();
        tabelaClientes.add(novoCliente);
        tabelaClientes.setName("tabelaClientes");

        return tabelaClientes;
    }

    public JPanel criaJanela3() {
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
        linha1.add(new JLabel("Codigo do App"));
        cbCodigoApp = new JComboBox<>();
        catApps.getStream().forEach(a -> cbCodigoApp.addItem(a.getCodigo()));
        linha1.add(cbCodigoApp);

        linha1.add(new JLabel("CPF"));
        cbCpfCliente = new JComboBox<>();
        catClientes.getStream().forEach(a -> cbCpfCliente.addItem(a.getCpf()));
        linha1.add(cbCpfCliente);

        JPanel linha2 = new JPanel(new FlowLayout(FlowLayout.LEADING));
        btAdd = new JButton("Nova Assinatura");
        btAdd.addActionListener(e -> adicionaAssinatura());
        linha2.add(btAdd);

        JPanel linha3 = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JTextField tf = new JTextField("Codigo");
        JButton delete = new JButton("Cancela");
        delete.addActionListener(e -> cancelaAssinatura(Integer.valueOf(tf.getText())));
        linha3.add(delete);
        linha3.add(tf);

        painel.add(linha1);
        painel.add(linha2);
        painel.add(linha3);
        return painel;
    }

    public void adicionaApp() {
        int codigo = countApp;
        String nome = tfNome.getText();
        String preco = tfPreco.getText();
        Aplicativo.SO so = (Aplicativo.SO) cbSo.getSelectedItem();
        Aplicativo novo = new Aplicativo(codigo, nome, preco, so);
        catApps.cadastra(novo);
        catAppsVM.fireTableDataChanged();
        cbCodigoApp.addItem(codigo);
        countApp++;
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
        int codigo = countAssin;
        int codigoApp = (Integer) cbCodigoApp.getSelectedItem();
        String cpf = cbCpfCliente.getSelectedItem().toString();
        String dataInicio = dia + "/" + mes + "/" + ano;
        String dataEncerra = "00/00";
        String status = "Ativo";

        Assinatura novo = new Assinatura(codigo, codigoApp, cpf, dataInicio, dataEncerra, status);
        catAssin.cadastra(novo);
        catAssinVM.fireTableDataChanged();
        countAssin++;
    }

    public void cancelaApp(int codigo) {
        catApps.remove(codigo);
        catAppsVM.fireTableDataChanged();
        catAssin.getStream().filter(a -> a.getCodigoApp() == codigo).forEach(a -> cancelaAssinatura(a.getCodigo()));
    }

    public void cancelaCliente(String cpf) {
        catClientes.remove(cpf);
        catClienteVM.fireTableDataChanged();
        catAssin.getStream().filter(a -> a.getCpfCliente().equals(cpf)).forEach(a -> cancelaAssinatura(a.getCodigo()));
    }

    public void cancelaAssinatura(int codigo) {
        catAssin.getStream().filter(a -> a.getCodigo() == codigo).forEach(a -> {
            a.setAtiva("Inativo");
            a.setDataEncerra(dia + "/" + mes + "/" + ano);
        });
        catAssinVM.fireTableDataChanged();
    }

    public void cobrar() {
        CatalogoCobranca cobrancas = new CatalogoCobranca();
        //para cada cliente
        catClientes.getStream().forEach(c -> {
            // pega as assinaturas ativas relacionadas ao cliente
            catAssin.getStream().filter(a -> a.getCpfCliente().equals(c.getCpf()) && a.isAtiva().equals("Ativo"))
                    .forEach(a -> {
                        // pega o valor do app relaciondo a esta assinatura
                        Double valor = Double.parseDouble(catApps.getStream()
                                .filter(ap -> ap.getCodigo() == a.getCodigoApp()).findAny().get().getPreco());
                        // cadastra nova cobranca com o valor
                        cobrancas.cadastra(new Cobranca(c.getNome(), c.getEmail(), valor));
                    });
        });
        CobrancasViewModel cVM = new CobrancasViewModel(cobrancas);
        JTable aux2 = new JTable(cVM);
        JScrollPane sp = new JScrollPane(aux2);
        JOptionPane.showMessageDialog(null, sp, "Cobrancas", JOptionPane.PLAIN_MESSAGE);
    }

    public Double rendimentoPorSO(Aplicativo.SO so) {
        List<Aplicativo> apps = new LinkedList<>();
        //pega as assinaturas ativa
        catAssin.getStream().filter(a->a.isAtiva().equals("Ativo")).forEach(a -> {
            //soma os valores dos aplicativos relacionados a assinatura
            catApps.getStream().filter(ap -> ap.getCodigo()==a.getCodigoApp()).filter(ap -> ap.getSo().equals(so)).forEach(ap -> apps.add(ap));
        });
        Double sum = apps.stream().mapToDouble(d -> Double.parseDouble(d.getPreco())).sum();
        return sum;
    }

    public void rendimentosTotais() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.PAGE_AXIS));

        Double rIOS = rendimentoPorSO(Aplicativo.SO.IOS);
        Double rAndroid = rendimentoPorSO(Aplicativo.SO.Android);

        JPanel linha1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
        linha1.add(new JLabel("IOS: " + rIOS));

        JPanel linha2 = new JPanel(new FlowLayout(FlowLayout.LEADING));
        linha2.add(new JLabel("Android: " + rAndroid));

        JPanel linha3 = new JPanel(new FlowLayout(FlowLayout.LEADING));
        linha3.add(new JLabel("Total: " + (rAndroid + rIOS)));

        painel.add(linha1);
        painel.add(linha2);
        painel.add(linha3);

        JOptionPane.showMessageDialog(null, painel, "Rendimentos Totais", JOptionPane.PLAIN_MESSAGE);
    }

    public void rendimentoPorApp() {
        // cria painel
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.PAGE_AXIS));
        catApps.getStream().forEach(ap -> {
            // verifica dentre as assinaturas ativas se app se repete
            List<Integer> repetidos = new LinkedList<>();
            catAssin.getStream().filter(a -> a.getCodigoApp() == ap.getCodigo() && a.isAtiva().equals("Ativo"))
                    .forEach(a -> repetidos.add(a.getCodigoApp()));
            // multipica preco pelo numero de repeticoes
            Double valor = Double.parseDouble(ap.getPreco()) * repetidos.size();
            // adiciona no painel o nome e o valor total
            painel.add(new JLabel(ap.getNome() + ": " + valor));
        });
        JOptionPane.showMessageDialog(null, painel, "Rendimentos por App", JOptionPane.PLAIN_MESSAGE);
    }

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.criaJanela();
    }
}

