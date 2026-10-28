package ec.edu.vienesa;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.*;
public class DemoUnidad2 {
 static String env(String n,String d){String v=System.getenv(n);return v==null||v.isBlank()?d:v;}
 public static EntityManagerFactory conectar(){
  String clave=System.getenv("DB_PASSWORD");
  if(clave==null){javax.swing.JPasswordField campo=new javax.swing.JPasswordField();int r=javax.swing.JOptionPane.showConfirmDialog(null,campo,"Clave PostgreSQL",javax.swing.JOptionPane.OK_CANCEL_OPTION);if(r!=0)throw new IllegalStateException("Inicio cancelado");clave=new String(campo.getPassword());}
  return Persistence.createEntityManagerFactory("vienesaPU",Map.of("jakarta.persistence.jdbc.url",env("DB_URL","jdbc:postgresql://localhost:5432/vienesa_u2"),"jakarta.persistence.jdbc.user",env("DB_USER","postgres"),"jakarta.persistence.jdbc.password",clave,"hibernate.hbm2ddl.auto",env("DB_DDL","update"),"hibernate.show_sql","false"));
 }
 public static void sembrar(EntityManager em){
  if(em.createQuery("SELECT COUNT(p) FROM Producto p",Long.class).getSingleResult()!=0)return;
  Categoria emb=new Categoria("Embutidos"), ahu=new Categoria("Ahumados");em.persist(emb);em.persist(ahu);
  Producto p1=new Producto("Salchicha Vienesa","500 g",new BigDecimal("3.50"),20);
  Producto p2=new Producto("Jamon","250 g",new BigDecimal("4.80"),15);
  Producto p3=new Producto("Chorizo ahumado","400 g",new BigDecimal("5.20"),12);
  emb.agregarProducto(p1);emb.agregarProducto(p2);ahu.agregarProducto(p3);em.persist(p1);em.persist(p2);em.persist(p3);
  Cliente ana=new Cliente("Ana Demo","ana@example.com","0991234567");em.persist(ana);
  Factura f=new Factura();ana.agregarFactura(f);f.agregarDetalle(new DetalleFactura(p1,2));f.agregarDetalle(new DetalleFactura(p3,1));em.persist(f);
 }
 static void imprimir(String nombre,List<?> filas){System.out.println(nombre+":");for(Object x:filas)System.out.println("  "+(x instanceof Object[]?Arrays.toString((Object[])x):x));}
 public static void ejecutar(EntityManager em){
  imprimir("1 SELECT",em.createQuery(ConsultasU2.TODOS).getResultList());
  imprimir("2 WHERE parametrizado",ConsultasU2.porCategoria(em,"Embutidos"));
  imprimir("3 JOIN",em.createQuery(ConsultasU2.JOIN).getResultList());
  imprimir("4 GROUP BY y COUNT",em.createQuery(ConsultasU2.GRUPO).getResultList());
  imprimir("5 Subconsulta",em.createQuery(ConsultasU2.SUBCONSULTA).getResultList());
  System.out.println("6 SUM: "+em.createQuery(ConsultasU2.TOTAL).getSingleResult());
  em.getTransaction().begin();
  int u=em.createQuery(ConsultasU2.UPDATE).setParameter("cantidad",1).setParameter("nombre","Jamon").executeUpdate();
  em.clear();System.out.println("7 UPDATE: filas="+u+"; stock temporal="+em.createQuery("SELECT p.stock FROM Producto p WHERE p.nombre='Jamon'",Integer.class).getSingleResult());
  em.getTransaction().rollback();em.clear();
  em.getTransaction().begin();Cliente temporal=new Cliente("Temporal Demo","temporal@example.com","0997654321");em.persist(temporal);em.flush();
  int d=em.createQuery(ConsultasU2.DELETE).setParameter("correo","temporal@example.com").executeUpdate();em.clear();
  System.out.println("8 DELETE: filas="+d+"; restantes temporales="+em.createQuery("SELECT COUNT(c) FROM Cliente c WHERE c.correo='temporal@example.com'",Long.class).getSingleResult());em.getTransaction().rollback();em.clear();
  em.getTransaction().begin();Producto nuevo=new Producto("Prueba ciclo","unidad",new BigDecimal("1.00"),1);
  System.out.println("Ciclo nueva: contains="+em.contains(nuevo));em.persist(nuevo);System.out.println("Ciclo gestionada: contains="+em.contains(nuevo));em.flush();em.detach(nuevo);System.out.println("Ciclo separada: contains="+em.contains(nuevo));Producto gestionada=em.merge(nuevo);System.out.println("merge: original="+em.contains(nuevo)+", copia="+em.contains(gestionada));em.remove(gestionada);em.flush();System.out.println("Ciclo eliminada: find="+em.find(Producto.class,gestionada.getId()));em.getTransaction().rollback();
 }
 public static void main(String[] args){try(EntityManagerFactory emf=conectar();EntityManager em=emf.createEntityManager()){
  em.getTransaction().begin();try{sembrar(em);em.getTransaction().commit();}catch(RuntimeException e){if(em.getTransaction().isActive())em.getTransaction().rollback();throw e;}
  ejecutar(em);
 }}
}
