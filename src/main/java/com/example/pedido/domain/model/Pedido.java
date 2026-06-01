package com.example.pedido.domain.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "PEDIDO")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_pedido", nullable = false, updatable = false)
    private LocalDateTime dataPedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPedido status;

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    protected Pedido() {
    }

    public Pedido(StatusPedido status) {
        this.status = status;
        this.valorTotal = BigDecimal.ZERO;
        this.itens = new ArrayList<>();
    }

    public static Pedido novoPedido() {
        return new Pedido(StatusPedido.CRIADO);
    }

    @PrePersist
    private void prePersist() {
        if (this.dataPedido == null) {
            this.dataPedido = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = StatusPedido.CRIADO;
        }
        if (this.valorTotal == null) {
            this.valorTotal = BigDecimal.ZERO;
        }
    }

    public void adicionarItem(ItemPedido item) {
        if (item == null) {
            throw new IllegalArgumentException("Item do pedido não pode ser nulo.");
        }

        item.associarAoPedido(this);
        this.itens.add(item);
        recalcularValorTotal();
    }

    public void adicionarItem(String nomeProduto, Integer quantidade, BigDecimal preco) {
        ItemPedido item = ItemPedido.novoItem(nomeProduto, quantidade, preco);
        adicionarItem(item);
    }

    public void removerItem(ItemPedido item) {
        if (item == null) {
            return;
        }

        this.itens.remove(item);
        item.associarAoPedido(null);
        recalcularValorTotal();
    }

    public void recalcularValorTotal() {
        this.valorTotal = this.itens.stream()
                .map(ItemPedido::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean possuiItens() {
        return !this.itens.isEmpty();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens.clear();

        if (itens != null) {
            for (ItemPedido item : itens) {
                adicionarItem(item);
            }
        } else {
            this.valorTotal = BigDecimal.ZERO;
        }
    }
}