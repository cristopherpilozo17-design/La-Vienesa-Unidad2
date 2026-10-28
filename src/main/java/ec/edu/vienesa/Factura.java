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

@Entity @Table(name="factura") @Getter @Setter @NoArgsConstructor
public class Factura {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @NotNull @Column(nullable=false) private LocalDateTime fecha=LocalDateTime.now();
 @NotNull @ManyToOne(fetch=FetchType.LAZY,optional=false)
 @JoinColumn(name="cliente_id",nullable=false) private Cliente cliente;
 @OneToMany(mappedBy="factura",cascade=CascadeType.ALL,orphanRemoval=true)
 @Setter(lombok.AccessLevel.NONE) private List<DetalleFactura> detalles=new ArrayList<>();
 public void agregarDetalle(DetalleFactura d){
  if(d.getFactura()!=null)d.getFactura().getDetalles().remove(d);
  if(!detalles.contains(d))detalles.add(d);d.setFactura(this);
 }
 public BigDecimal total(){return detalles.stream().map(DetalleFactura::subtotal).reduce(BigDecimal.ZERO,BigDecimal::add);}
}
