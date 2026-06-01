package com.example.pedido.domain.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ITEM_PEDIDO")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "nome_produto", nullable = false, length = 100)
    private String nomeProduto;

    @Column(nullable = false)
    private BigDecimal preco;

    protected ItemPedido() {
        // Construtor protegido para o JPA
    }

    private ItemPedido(String nomeProduto, Integer quantidade, BigDecimal preco) {
        validar(nomeProduto, quantidade, preco);
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public static ItemPedido novoItem(String nomeProduto, Integer quantidade, BigDecimal preco) {
        return new ItemPedido(nomeProduto, quantidade, preco);
    }

    public BigDecimal calcularSubtotal() {
        return preco.multiply(BigDecimal.valueOf(quantidade));
    }

    public void associarAoPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public void alterarQuantidade(Integer quantidade) {
        validarQuantidade(quantidade);
        this.quantidade = quantidade;
    }

    public void alterarPreco(BigDecimal preco) {
        validarPreco(preco);
        this.preco = preco;
    }

    private void validar(String nomeProduto, Integer quantidade, BigDecimal preco) {
        validarNomeProduto(nomeProduto);
        validarQuantidade(quantidade);
        validarPreco(preco);
    }

    private void validarNomeProduto(String nomeProduto) {
        if (nomeProduto == null || nomeProduto.isBlank()) {
            throw new IllegalArgumentException("nomeProduto é obrigatório.");
        }
    }

    private void validarQuantidade(Integer quantidade) {
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("quantidade deve ser maior que zero.");
        }
    }

    private void validarPreco(BigDecimal preco) {
        if (preco == null || preco.signum() <= 0) {
            throw new IllegalArgumentException("preco deve ser maior que zero.");
        }
    }

    public Long getId() {
        return id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public BigDecimal getPreco() {
        return preco;
    }
}