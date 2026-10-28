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

@Entity @Table(name="categoria") @Getter @Setter @NoArgsConstructor
public class Categoria {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @NotBlank @Size(max=60) @Column(nullable=false,length=60,unique=true) private String nombre;
 @OneToMany(mappedBy="categoria") @Setter(lombok.AccessLevel.NONE)
 private List<Producto> productos=new ArrayList<>();
 public Categoria(String nombre){this.nombre=nombre;}
 public void agregarProducto(Producto p){
  if(p.getCategoria()!=null)p.getCategoria().getProductos().remove(p);
  if(!productos.contains(p))productos.add(p);p.setCategoria(this);
 }
}
