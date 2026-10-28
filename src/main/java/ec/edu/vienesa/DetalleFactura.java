package ec.edu.vienesa;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name="detalle_factura") @Getter @Setter @NoArgsConstructor
public class DetalleFactura {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @NotNull @ManyToOne(fetch=FetchType.LAZY,optional=false)
 @JoinColumn(name="factura_id",nullable=false) private Factura factura;
 @NotNull @ManyToOne(fetch=FetchType.LAZY,optional=false)
 @JoinColumn(name="producto_id",nullable=false) private Producto producto;
 @NotNull @Min(1) @Column(nullable=false) private Integer cantidad;
 @NotNull @DecimalMin("0.00") @Digits(integer=8,fraction=2)
 @Column(nullable=false,precision=10,scale=2) private BigDecimal precioUnitario;
 public DetalleFactura(Producto producto,int cantidad){this.producto=producto;this.cantidad=cantidad;this.precioUnitario=producto.getPrecio();}
 public BigDecimal subtotal(){return precioUnitario.multiply(BigDecimal.valueOf(cantidad));}
}
