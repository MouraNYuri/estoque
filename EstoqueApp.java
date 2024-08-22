import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.SQLException;
import java.util.List;

public class EstoqueApp {
    private JFrame frame;
    private JTextField nomeField, descricaoField, quantidadeField, precoField, marcaField, saidasField, fotoField;
    private JTextField removerNomeField, buscarNomeField;
    private JTextField atualizarNomeField, novaDescricaoField, novaQuantidadeField, novoPrecoField, novaMarcaField, novasSaidasField;
    private JTextArea produtoArea;
    private JLabel fotoLabel;
    private ProdutoDAO produtoDAO;
    private JButton selecionarFotoButton;
    private String caminhoFoto;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EstoqueApp().criarGUI());
    }

    // Configuração da interface gráfica
    private void criarGUI() {
        produtoDAO = new ProdutoDAO();

        frame = new JFrame("Sistema de Monitoramento de Estoque");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 800);
        frame.setLayout(new BorderLayout());

        // Definição de fontes
        Font tituloFonte = new Font("Arial", Font.BOLD, 24);
        Font labelFonte = new Font("Arial", Font.PLAIN, 18);
        Font inputFonte = new Font("Arial", Font.PLAIN, 16);
        Font produtoFonte = new Font("Arial", Font.PLAIN, 18);

        // Painel principal com campos para operação
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBorder(new EmptyBorder(20, 10, 20, 10));
        frame.add(painelPrincipal, BorderLayout.NORTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Adiciona os painéis para operações CRUD
        adicionarPainel(painelPrincipal, "Adicionar Produto", new AdicionarProdutoListener(), labelFonte, inputFonte, gbc, tituloFonte);
        adicionarPainel(painelPrincipal, "Remover Produto", new RemoverProdutoListener(), labelFonte, inputFonte, gbc, tituloFonte);
        adicionarPainel(painelPrincipal, "Buscar Produto", new BuscarProdutoListener(), labelFonte, inputFonte, gbc, tituloFonte);
        adicionarPainel(painelPrincipal, "Atualizar Produto", new AtualizarProdutoListener(), labelFonte, inputFonte, gbc, tituloFonte);

        // Área de texto para exibição de produtos
        produtoArea = new JTextArea();
        produtoArea.setEditable(false);
        produtoArea.setFont(produtoFonte);

        JScrollPane scrollPane = new JScrollPane(produtoArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Painel para exibir a foto do produto
        JPanel painelFoto = new JPanel();
        painelFoto.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Foto do Produto", TitledBorder.LEFT, TitledBorder.TOP, tituloFonte));
        fotoLabel = new JLabel();
        fotoLabel.setPreferredSize(new Dimension(200, 200));
        painelFoto.add(fotoLabel);
        frame.add(painelFoto, BorderLayout.EAST);

        frame.setVisible(true);
        listarProdutos();
    }

    // Adiciona um painel com campos e botões para operações CRUD
    private void adicionarPainel(JPanel painelPrincipal, String titulo, ActionListener listener, Font labelFonte, Font inputFonte, GridBagConstraints gbc, Font tituloFonte) {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), titulo, TitledBorder.LEFT, TitledBorder.TOP, tituloFonte));

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;

        switch (titulo) {
            case "Adicionar Produto":
                addCampo(painel, "Nome:", nomeField = new JTextField(20), labelFonte, inputFonte, gbc);
                addCampo(painel, "Descrição:", descricaoField = new JTextField(20), labelFonte, inputFonte, gbc);
                addCampo(painel, "Quantidade:", quantidadeField = new JTextField(20), labelFonte, inputFonte, gbc);
                addCampo(painel, "Preço:", precoField = new JTextField(20), labelFonte, inputFonte, gbc);
                addCampo(painel, "Marca:", marcaField = new JTextField(20), labelFonte, inputFonte, gbc);
                addCampo(painel, "Quantidade de Saídas:", saidasField = new JTextField(20), labelFonte, inputFonte, gbc);
                addCampo(painel, "Foto:", fotoField = new JTextField(20), labelFonte, inputFonte, gbc);

                selecionarFotoButton = new JButton("Selecionar Foto");
                selecionarFotoButton.setFont(labelFonte);
                selecionarFotoButton.addActionListener(e -> selecionarFoto());
                
                gbc.gridy++;
                gbc.gridx = 1;
                painel.add(selecionarFotoButton, gbc);
                break;
            case "Remover Produto":
                addCampo(painel, "Nome:", removerNomeField = new JTextField(20), labelFonte, inputFonte, gbc);
                break;
            case "Buscar Produto":
                addCampo(painel, "Nome:", buscarNomeField = new JTextField(20), labelFonte, inputFonte, gbc);
                break;
            case "Atualizar Produto":
                addCampo(painel, "Nome:", atualizarNomeField = new JTextField(20), labelFonte, inputFonte, gbc);
                addCampo(painel, "Nova Descrição:", novaDescricaoField = new JTextField(20), labelFonte, inputFonte, gbc);
                addCampo(painel, "Nova Quantidade:", novaQuantidadeField = new JTextField(20), labelFonte, inputFonte, gbc);
                addCampo(painel, "Novo Preço:", novoPrecoField = new JTextField(20), labelFonte, inputFonte, gbc);
                addCampo(painel, "Nova Marca:", novaMarcaField = new JTextField(20), labelFonte, inputFonte, gbc);
                addCampo(painel, "Nova Quantidade de Saídas:", novasSaidasField = new JTextField(20), labelFonte, inputFonte, gbc);
                addCampo(painel, "Foto:", fotoField = new JTextField(20), labelFonte, inputFonte, gbc);
                selecionarFotoButton = new JButton("Selecionar Foto");
                selecionarFotoButton.setFont(labelFonte);
                selecionarFotoButton.addActionListener(e -> selecionarFoto());
                
                gbc.gridy++;
                gbc.gridx = 1;
                painel.add(selecionarFotoButton, gbc);
                break;
        }

        JButton botao = new JButton(titulo.split(" ")[0] + " Produto");
        botao.setFont(labelFonte);
        botao.addActionListener(listener);
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(botao, gbc);

        painelPrincipal.add(painel);
    }

    // Adiciona um campo ao painel
    private void addCampo(JPanel painel, String labelTexto, JTextField campoTexto, Font labelFonte, Font inputFonte, GridBagConstraints gbc) {
        gbc.gridx = 0;
        JLabel label = new JLabel(labelTexto);
        label.setFont(labelFonte);
        painel.add(label, gbc);

        gbc.gridx = 1;
        campoTexto.setFont(inputFonte);
        painel.add(campoTexto, gbc);

        gbc.gridy++;
    }

    // Lista todos os produtos e exibe suas informações
    private void listarProdutos() {
        try {
            List<Produto> produtos = produtoDAO.listarProdutos();
            produtoArea.setText("");
            for (Produto produto : produtos) {
                produtoArea.append(produto.toString() + "\n");
                exibirFotoProduto(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Exibe a foto do produto
    private void exibirFotoProduto(Produto produto) {
        if (produto.getFoto() != null) {
            ImageIcon fotoIcon = new ImageIcon(produto.getFoto());
            Image imagem = fotoIcon.getImage();
            Image novaImagem = imagem.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            fotoIcon = new ImageIcon(novaImagem);
            fotoLabel.setIcon(fotoIcon);
        } else {
            fotoLabel.setIcon(null);
        }
    }

    // Abre o seletor de arquivos para escolher a foto
    private void selecionarFoto() {
        JFileChooser fileChooser = new JFileChooser();
        int resultado = fileChooser.showOpenDialog(frame);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            caminhoFoto = fileChooser.getSelectedFile().getAbsolutePath();
            fotoField.setText(caminhoFoto);
        }
    }

    // Valida se todos os campos estão preenchidos
    private void validarCampos(JTextField... campos) throws NumberFormatException {
        for (JTextField campo : campos) {
            if (campo.getText().trim().isEmpty()) {
                throw new NumberFormatException("Todos os campos devem ser preenchidos.");
            }
        }
    }

    // Carrega a foto a partir do caminho fornecido
    private byte[] carregarFoto(String caminho) {
        if (caminho == null || caminho.isEmpty()) return null;
        try (FileInputStream fis = new FileInputStream(caminho)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Adiciona um produto
    private class AdicionarProdutoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                validarCampos(nomeField, descricaoField, quantidadeField, precoField, marcaField, saidasField);
                String nome = nomeField.getText().trim();
                String descricao = descricaoField.getText().trim();
                int quantidade = Integer.parseInt(quantidadeField.getText().trim());
                double preco = Double.parseDouble(precoField.getText().trim());
                String marca = marcaField.getText().trim();
                int saídas = Integer.parseInt(saidasField.getText().trim());
                byte[] foto = carregarFoto(caminhoFoto);

                Produto produto = new Produto(nome, descricao, quantidade, preco, marca, saídas, foto);
                produtoDAO.adicionarProduto(produto);
                listarProdutos();
            } catch (NumberFormatException | SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Erro ao adicionar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Remove um produto
    private class RemoverProdutoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                validarCampos(removerNomeField);
                String nome = removerNomeField.getText().trim();
                produtoDAO.removerProduto(nome);
                listarProdutos();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Erro ao remover produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Busca um produto
    private class BuscarProdutoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                validarCampos(buscarNomeField);
                String nome = buscarNomeField.getText().trim();
                Produto produto = produtoDAO.buscarProduto(nome);
                produtoArea.setText(produto != null ? produto.toString() : "Produto não encontrado.");
                exibirFotoProduto(produto);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Erro ao buscar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Atualiza um produto
    private class AtualizarProdutoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                validarCampos(atualizarNomeField, novaDescricaoField, novaQuantidadeField, novoPrecoField, novaMarcaField, novasSaidasField);
                String nome = atualizarNomeField.getText().trim();
                String novaDescricao = novaDescricaoField.getText().trim();
                int novaQuantidade = Integer.parseInt(novaQuantidadeField.getText().trim());
                double novoPreco = Double.parseDouble(novoPrecoField.getText().trim());
                String novaMarca = novaMarcaField.getText().trim();
                int novasSaidas = Integer.parseInt(novasSaidasField.getText().trim());
                byte[] novaFoto = carregarFoto(caminhoFoto);

                Produto produto = new Produto(nome, novaDescricao, novaQuantidade, novoPreco, novaMarca, novasSaidas, novaFoto);
                produtoDAO.atualizarProduto(produto);
                listarProdutos();
            } catch (NumberFormatException | SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Erro ao atualizar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
