package ec.edu.vienesa;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Getter @Setter
@Entity
@Table(name = "producto")
public class Producto {
    @Version private Long version;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="categoria_id")
    private Categoria categoria;
    @Column(nullable=false,updatable=false) private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
    @PrePersist private void alCrear(){creadoEn=LocalDateTime.now();actualizadoEn=creadoEn;}
    @PreUpdate private void alActualizar(){actualizadoEn=LocalDateTime.now();}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    @NotBlank @Size(max=100)
    private String nombre;
    @Column(nullable = false, length = 40)
    @NotBlank @Size(max=40)
    private String presentacion;
    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull @DecimalMin("0.00") @Digits(integer=8,fraction=2)
    private BigDecimal precio;
    @Column(nullable = false)
    @NotNull @Min(0)
    private Integer stock;

    @Column(nullable = false)
    private boolean activo = true;
    public boolean isActivo() { return activo; }
    public void setActivo(boolean valor) { activo = valor; }

    public Producto() { }
    public Producto(String nombre, String presentacion, BigDecimal precio, int stock) {
        this.nombre = nombre;
        this.presentacion = presentacion;
        setPrecio(precio);
        setStock(stock);
    }
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getPresentacion() { return presentacion; }
    public BigDecimal getPrecio() { return precio; }
    public Integer getStock() { return stock; }
    public void setNombre(String valor) { nombre = valor; }
    public void setPresentacion(String valor) { presentacion = valor; }
    public void setPrecio(BigDecimal valor) {
        if (valor == null || valor.signum() < 0 || valor.scale() > 2)
            throw new IllegalArgumentException("Precio: importe no negativo con hasta dos decimales.");
        precio = valor;
    }
    public void setStock(int valor) {
        if (valor < 0) throw new IllegalArgumentException("El stock no puede ser negativo.");
        stock = valor;
    }
    @Override public String toString() {
        return "Producto{id=" + id + ", nombre='" + nombre + "', presentacion='"
            + presentacion + "', precio=" + precio + ", stock=" + stock + "}";
    }
}
