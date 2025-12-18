import java.util.Scanner;
public class Main
{
static int arroz =0;
static int feijao =0;
static int farinha =0;
static int leite =0;
static int escolha=0;
static int narroz =0;
static int nfeijao =0;
static int nfarinha =0;
static int nleite =0;
static int cpf=0;
static int total=0;


public static void cad (){
Scanner scanner = new Scanner(System.in);
System.out.println("insira a quantidade de arroz no estoque: ");
arroz = scanner .nextInt();
System.out.println("insira a quantidade de feijao no estoque: ");
feijao = scanner .nextInt();
System.out.println("insira a quantidade de farinha no estoque: ");
farinha = scanner .nextInt();
System.out.println("insira a quantidade de leite no estoque: ");
leite = scanner .nextInt();
}
public static void list(String[] arg){
Scanner scanner = new Scanner (System.in);
System.out.println("Insira quantas unidades de arroz desejas: ");
narroz= scanner .nextInt();
System.out.println("Insira quantas unidades de feijao desejas: ");
nfeijao=scanner .nextInt();
System.out.println("Insira quantas unidades de farinha desejas: ");
nfarinha=scanner. nextInt();
System.out.println("Insira quantas unidades de leite desejas: ");
nleite=scanner .nextInt();
scanner.nextLine();
System.out.println("insira seu nome: ");
String nome = scanner.nextLine();
System.out.println("insira seu cpf");
cpf=scanner .nextInt();
scanner.nextLine();
System.out.println("Insira o método de pagamento: ");

String met = scanner .nextLine();
total=(nleite*10)+(nfeijao*5)+(narroz*5)+(nfarinha*6);
System.out.println("o preço total foi de: "+total);

}

public static void main(String[] args) {
while(true){
System.out.println("1) Cadastrar\n2) Fazer a lista e pagar\n0) Sair");
Scanner scanner = new Scanner(System.in);
escolha = scanner .nextInt();

switch(escolha){
case 1:
cad();
break;
case 2:
list(new String[0]);
break;
case 0:
System.exit(0);
}
}

}
}