package ec.edu.vienesa;
import jakarta.persistence.*;
import jakarta.validation.*;
import java.util.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.io.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class Unidad2Test {
 @Test void relacionesConsultasCicloYValidacion() throws Exception {
  Map<String,Object> props=Map.of("jakarta.persistence.jdbc.driver","org.h2.Driver","jakarta.persistence.jdbc.url","jdbc:h2:mem:unidad2;MODE=PostgreSQL;DB_CLOSE_DELAY=-1","jakarta.persistence.jdbc.user","sa","jakarta.persistence.jdbc.password","","hibernate.hbm2ddl.auto","create-drop","hibernate.show_sql","false");
  try(EntityManagerFactory emf=Persistence.createEntityManagerFactory("vienesaPU",props);EntityManager em=emf.createEntityManager()){
   em.getTransaction().begin();DemoUnidad2.sembrar(em);em.getTransaction().commit();em.clear();
   assertEquals(3,em.createQuery(ConsultasU2.TODOS).getResultList().size());
   assertEquals(List.of("Jamon","Salchicha Vienesa"),ConsultasU2.porCategoria(em,"Embutidos"));
   assertEquals(1,em.createQuery(ConsultasU2.JOIN).getResultList().size());
   assertEquals(2,em.createQuery(ConsultasU2.GRUPO).getResultList().size());
   assertEquals(List.of("Chorizo ahumado","Jamon"),em.createQuery(ConsultasU2.SUBCONSULTA,String.class).getResultList());
   assertEquals(0,new BigDecimal("12.20").compareTo((BigDecimal)em.createQuery(ConsultasU2.TOTAL).getSingleResult()));
   Factura factura=em.createQuery("SELECT DISTINCT f FROM Factura f JOIN FETCH f.detalles",Factura.class).getSingleResult();
   assertEquals(2,factura.getDetalles().size());assertEquals(0,new BigDecimal("12.20").compareTo(factura.total()));
   PrintStream original=System.out;ByteArrayOutputStream buffer=new ByteArrayOutputStream();
   try(PrintStream captura=new PrintStream(buffer,true,java.nio.charset.StandardCharsets.UTF_8)){System.setOut(captura);DemoUnidad2.ejecutar(em);}finally{System.setOut(original);}
   String salida=buffer.toString(java.nio.charset.StandardCharsets.UTF_8);System.out.print(salida);
   assertTrue(salida.contains("stock temporal=16"));assertTrue(salida.contains("restantes temporales=0"));assertTrue(salida.contains("merge: original=false, copia=true"));assertTrue(salida.contains("Ciclo eliminada: find=null"));
   assertEquals(15,em.createQuery("SELECT p.stock FROM Producto p WHERE p.nombre='Jamon'",Integer.class).getSingleResult());
   Files.createDirectories(java.nio.file.Path.of("evidencias"));Files.writeString(java.nio.file.Path.of("evidencias/resultado_prueba_H2.txt"),"VALIDACION AUTOMATICA EN H2 (MODO POSTGRESQL). NO ES EVIDENCIA DE POSTGRESQL.\n"+salida);
   try(ValidatorFactory vf=Validation.buildDefaultValidatorFactory()){
    Cliente invalido=new Cliente("A","correo-invalido","123");assertEquals(3,vf.getValidator().validate(invalido).size());
    DetalleFactura d=factura.getDetalles().get(0);d.setCantidad(0);assertFalse(vf.getValidator().validate(d).isEmpty());
   }
  }
 }
}

