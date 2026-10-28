package ec.edu.vienesa;
import jakarta.persistence.*;
import java.util.List;
public class ConsultasU2 {
 public static final String TODOS="SELECT p.nombre FROM Producto p WHERE p.activo=true ORDER BY p.nombre";
 public static final String CATEGORIA="SELECT p.nombre FROM Producto p WHERE p.categoria.nombre = :categoria ORDER BY p.nombre";
 public static final String JOIN="SELECT f.id, c.nombre FROM Factura f JOIN f.cliente c ORDER BY f.id";
 public static final String GRUPO="SELECT c.nombre, COUNT(p) FROM Categoria c LEFT JOIN c.productos p GROUP BY c.nombre ORDER BY c.nombre";
 public static final String SUBCONSULTA="SELECT p.nombre FROM Producto p WHERE p.precio > (SELECT AVG(p2.precio) FROM Producto p2) ORDER BY p.nombre";
 public static final String TOTAL="SELECT SUM(d.cantidad * d.precioUnitario) FROM DetalleFactura d";
 public static final String UPDATE="UPDATE Producto p SET p.stock = p.stock + :cantidad, p.version = p.version + 1 WHERE p.nombre = :nombre";
 public static final String DELETE="DELETE FROM Cliente c WHERE c.correo = :correo";
 public static List<String> porCategoria(EntityManager em,String nombre){return em.createQuery(CATEGORIA,String.class).setParameter("categoria",nombre).getResultList();}
}
