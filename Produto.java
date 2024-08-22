import java.util.Objects;
import java.util.Arrays;

public class Produto {
    private final String nome;
    private final String descricao;
    private final int quantidade;
    private final double preco;
    private final String marca;
    private final int quantidadeSaidas;
    private final byte[] foto; // Campo para armazenar a imagem

    // Construtor da classe Produto com validação de dados
    public Produto(String nome, String descricao, int quantidade, double preco, String marca, int quantidadeSaidas, byte[] foto) {
        // Validações para garantir que os dados do produto são válidos
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto não pode ser vazio.");
        }
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição do produto não pode ser vazia.");
        }
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa.");
        }
        if (preco < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo.");
        }
        if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException("Marca do produto não pode ser vazia.");
        }
        if (quantidadeSaidas < 0) {
            throw new IllegalArgumentException("Quantidade de saídas não pode ser negativa.");
        }

        // Inicializa os campos finais da classe
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.preco = preco;
        this.marca = marca;
        this.quantidadeSaidas = quantidadeSaidas;
        this.foto = foto; // Inicializa o campo foto
    }

    // Getters para acessar os valores dos atributos
    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public String getMarca() {
        return marca;
    }

    public int getQuantidadeSaidas() {
        return quantidadeSaidas;
    }

    public byte[] getFoto() {
        return foto; // Getter para o campo foto
    }

    // Sobrescreve o método toString para representar o produto de forma legível
    @Override
    public String toString() {
        return String.format("Produto: %s | Descrição: %s | Quantidade: %d | Preço: R$ %.2f | Marca: %s | Quantidade de Saídas: %d",
                             nome, descricao, quantidade, preco, marca, quantidadeSaidas);
    }

    // Sobrescreve o método equals para comparar dois produtos de forma adequada
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Verifica se é o mesmo objeto
        if (o == null || getClass() != o.getClass()) return false; // Verifica se o objeto é da mesma classe
        Produto produto = (Produto) o; // Faz o cast do objeto para Produto
        // Compara os atributos dos produtos, incluindo o campo foto
        return quantidade == produto.quantidade &&
               Double.compare(produto.preco, preco) == 0 &&
               quantidadeSaidas == produto.quantidadeSaidas &&
               nome.equals(produto.nome) &&
               descricao.equals(produto.descricao) &&
               marca.equals(produto.marca) &&
               Arrays.equals(foto, produto.foto); // Compara arrays de bytes
    }

    // Sobrescreve o método hashCode para gerar um código hash consistente com o método equals
    @Override
    public int hashCode() {
        return Objects.hash(nome, descricao, quantidade, preco, marca, quantidadeSaidas, Arrays.hashCode(foto)); // Inclui o campo foto
    }
}
