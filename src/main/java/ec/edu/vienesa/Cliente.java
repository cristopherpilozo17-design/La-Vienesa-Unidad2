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

@Entity @Table(name="cliente") @Getter @Setter @NoArgsConstructor
public class Cliente {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @NotBlank @Size(min=3,max=80) @Column(nullable=false,length=80) private String nombre;
 @NotBlank @Email @Size(max=100) @Column(nullable=false,length=100) private String correo;
 @NotBlank @Pattern(regexp="[0-9]{10}") @Column(nullable=false,length=10) private String telefono;
 @OneToMany(mappedBy="cliente") @Setter(lombok.AccessLevel.NONE)
 private List<Factura> facturas=new ArrayList<>();
 public Cliente(String nombre,String correo,String telefono){this.nombre=nombre;this.correo=correo;this.telefono=telefono;}
 public void agregarFactura(Factura f){
  if(f.getCliente()!=null)f.getCliente().getFacturas().remove(f);
  if(!facturas.contains(f))facturas.add(f);f.setCliente(this);
 }
}
