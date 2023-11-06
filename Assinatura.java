public class Assinatura 
{
 private int codigo;
 private int codigoA;//código do aplicativo
 private String cpfC;//Cpf do cliente
 private String inicio;//incio da vigência, mês e ano
 private String encerramento;//final da vigência     
 public Assinatura(int codigo, int codigoA, String cpfC, String inicio, String encerramento)
 {
  this.codigo = codigo;
  this.codigoA = codigoA;
  this.cpfC = cpfC;
  this.inicio = inicio;
  this.encerramento = encerramento;
 }    
}
