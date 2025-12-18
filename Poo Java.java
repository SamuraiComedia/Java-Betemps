import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

// classe principal que estende jframe para criar a interface grafica
public class SammysApp extends JFrame {

    // --- classes de dominio (poo) ---

    // classe abstrata que serve de molde para todos os equipamentos
    // nao pode ser instanciada diretamente (abstracao)
    abstract static class Equipment {
        private int id; // identificador unico do tipo
        private String description; // nome do equipamento
        private double baseFee; // taxa fixa inicial
        private double hourlyRate; // valor cobrado por hora

        // construtor para inicializar os atributos comuns
        public Equipment(int id, String description, double baseFee, double hourlyRate) {
            this.id = id;
            this.description = description;
            this.baseFee = baseFee;
            this.hourlyRate = hourlyRate;
        }

        // metodos acessores (getters) para manter o encapsulamento
        public String getDescription() { return description; }
        public double getBaseFee() { return baseFee; }
        public double getHourlyRate() { return hourlyRate; }
    }

    // subclasses que herdam de equipment (heranca)
    // cada uma define seus proprios valores de taxa conforme a tabela do pdf
    static class JetSki extends Equipment {
        public JetSki() { super(1, "jet ski", 50.0, 30.0); }
    }

    static class PontoonBoat extends Equipment {
        public PontoonBoat() { super(2, "barco pontao", 40.0, 30.0); }
    }

    static class Kayak extends Equipment {
        public Kayak() { super(3, "caiaque", 20.0, 15.0); }
    }

    // classe que representa o contrato de aluguel
    static class Rental {
        private static int nextContractNumber = 100; // controle de id unico universal
        private String contractNumber;
        private int minutes;
        private double totalPrice;
        private Equipment equipment; // associacao entre rental e equipment

        // construtor que gera o numero do contrato automaticamente
        public Rental(int minutes, Equipment equipment) {
            this.contractNumber = "c" + nextContractNumber++;
            this.minutes = minutes;
            this.equipment = equipment;
            calculatePrice(); // calcula o valor no momento da criacao
        }

        // logica para calcular o preco total baseado em horas cheias
        private void calculatePrice() {
            // converte minutos para horas arredondando para cima
            double hours = Math.ceil(minutes / 60.0);
            this.totalPrice = equipment.getBaseFee() + (hours * equipment.getHourlyRate());
        }

        // sobrescrita do metodo tostring para exibir os dados do contrato
        @Override
        public String toString() {
            return String.format("[%s] %s - %d min - total: $%.2f", 
                    contractNumber, equipment.getDescription(), minutes, totalPrice);
        }
    }

    // --- interface grafica (gui) ---

    private JTextField txtMinutes; // campo para digitar o tempo
    private JComboBox<String> comboEquip; // selecao do equipamento
    private JTextArea areaLog; // area de texto para exibir resultados
    private ArrayList<Rental> rentalsList = new ArrayList<>(); // lista polimorfica de alugueis

    public SammysApp() {
        // configuracoes basicas da janela
        setTitle("sammy's seashore supplies - sistema de aluguer");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // painel superior para entrada de dados com gridlayout
        JPanel pnlInput = new JPanel(new GridLayout(4, 2, 5, 5));
        pnlInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // adicionando componentes de interface
        pnlInput.add(new JLabel("tempo de aluguer (minutos):"));
        txtMinutes = new JTextField();
        pnlInput.add(txtMinutes);

        pnlInput.add(new JLabel("tipo de equipamento:"));
        comboEquip = new JComboBox<>(new String[]{"jet ski", "barco pontao", "caiaque"});
        pnlInput.add(comboEquip);

        // botoes de acao
        JButton btnConfirm = new JButton("confirmar aluguel");
        JButton btnList = new JButton("listar tudo");
        pnlInput.add(btnConfirm);
        pnlInput.add(btnList);

        // area central com scroll para o log de contratos
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaLog);
        scroll.setBorder(BorderFactory.createTitledBorder("relatorio de contratos"));

        add(pnlInput, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // definicao de eventos usando expressoes lambda
        btnConfirm.addActionListener(e -> processarAluguer());
        btnList.addActionListener(e -> listarAlugueres());

        setLocationRelativeTo(null); // centraliza a janela na tela
    }

    // metodo para processar a entrada e criar um novo objeto rental
    private void processarAluguer() {
        try {
            int mins = Integer.parseInt(txtMinutes.getText());
            Equipment selecionado;

            // decide qual subclasse instanciar (polimorfismo)
            switch (comboEquip.getSelectedIndex()) {
                case 0: selecionado = new JetSki(); break;
                case 1: selecionado = new PontoonBoat(); break;
                default: selecionado = new Kayak(); break;
            }

            // cria o contrato e adiciona na lista
            Rental novoRental = new Rental(mins, selecionado);
            rentalsList.add(novoRental);
            
            // feedback visual para o usuario
            areaLog.append("sucesso: " + novoRental.toString() + "\n");
            txtMinutes.setText("");
            
        } catch (NumberFormatException ex) {
            // tratamento de erro caso o usuario nao digite numeros
            JOptionPane.showMessageDialog(this, "por favor, insira um numero valido de minutos.");
        }
    }

    // percorre a lista de alugueis e exibe na tela
    private void listarAlugueres() {
        areaLog.setText("--- listagem geral de alugueres ---\n");
        if (rentalsList.isEmpty()) {
            areaLog.append("nenhum contrato registrado.\n");
        } else {
            for (Rental r : rentalsList) {
                areaLog.append(r.toString() + "\n");
            }
        }
    }

    // ponto de entrada do programa
    public static void main(String[] args) {
        // garante que a interface rode na thread correta do swing
        SwingUtilities.invokeLater(() -> {
            new SammysApp().setVisible(true);
        });
    }
}