import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ProdutoDAO {
    private static final String CONFIG_FILE = "db.properties"; // Arquivo de configuração para as credenciais do banco de dados

    private final String url;
    private final String user;
    private final String password;

    // Construtor da classe ProdutoDAO para carregar as configurações do banco de dados
    public ProdutoDAO() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Configuração de banco de dados não encontrada.");
            }
            properties.load(input);
            url = properties.getProperty("db.url");
            user = properties.getProperty("db.user");
            password = properties.getProperty("db.password");
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar configurações de banco de dados.", e);
        }
    }

    // Adiciona um novo produto ao banco de dados
    public void adicionarProduto(Produto produto) throws SQLException {
        String sql = "INSERT INTO produto (nome, descricao, quantidade, preco, marca, quantidade_saidas, foto) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setDouble(4, produto.getPreco());
            stmt.setString(5, produto.getMarca());
            stmt.setInt(6, produto.getQuantidadeSaidas());
            stmt.setBytes(7, produto.getFoto()); // Define o valor do campo foto
            stmt.executeUpdate();
        }
    }

    // Remove um produto do banco de dados baseado no nome
    public void removerProduto(String nome) throws SQLException {
        String sql = "DELETE FROM produto WHERE nome = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Produto não encontrado para remoção.");
            }
        }
    }

    // Lista todos os produtos presentes no banco de dados
    public List<Produto> listarProdutos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                produtos.add(mapResultSetToProduto(rs));
            }
        }
        return produtos;
    }

    // Busca um produto no banco de dados pelo nome
    public Produto buscarProduto(String nome) throws SQLException {
        String sql = "SELECT * FROM produto WHERE nome = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduto(rs);
                }
                return null; // Retorna null se o produto não for encontrado
            }
        }
    }

    // Atualiza as informações de um produto no banco de dados
    public void atualizarProduto(Produto produto) throws SQLException {
        String sql = "UPDATE produto SET descricao = ?, quantidade = ?, preco = ?, marca = ?, quantidade_saidas = ?, foto = ? WHERE nome = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getDescricao());
            stmt.setInt(2, produto.getQuantidade());
            stmt.setDouble(3, produto.getPreco());
            stmt.setString(4, produto.getMarca());
            stmt.setInt(5, produto.getQuantidadeSaidas());
            stmt.setBytes(6, produto.getFoto()); // Define o valor do campo foto
            stmt.setString(7, produto.getNome());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Produto não encontrado para atualização.");
            }
        }
    }

    // Mapeia o ResultSet para um objeto Produto
    private Produto mapResultSetToProduto(ResultSet rs) throws SQLException {
        String nome = rs.getString("nome");
        String descricao = rs.getString("descricao");
        int quantidade = rs.getInt("quantidade");
        double preco = rs.getDouble("preco");
        String marca = rs.getString("marca");
        int quantidadeSaidas = rs.getInt("quantidade_saidas");
        byte[] foto = rs.getBytes("foto"); // Mapeia o campo foto
        return new Produto(nome, descricao, quantidade, preco, marca, quantidadeSaidas, foto);
    }
}
