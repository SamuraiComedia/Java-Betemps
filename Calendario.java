
import java.util.Scanner;

public class Main
{

static int dia=0;
static int mes=0;
static int ano=0;


public static void ndia(){
Scanner scanner = new Scanner(System.in);

System.out.println("insira o dia de hoje: ");
dia=scanner .nextInt();

}
public static void nmes(){
Scanner scanner = new Scanner(System.in);
System.out.println("insira o mes de hoje: ");
mes=scanner .nextInt();
}
public static void nano(){
Scanner scanner = new Scanner(System.in);
System.out.println("insira o ano de hoje: ");
ano=scanner .nextInt();
}
public static void ntudo(){
Scanner scanner = new Scanner(System.in);
System.out.println("insira o dia de hoje: ");
dia=scanner .nextInt();
System.out.println("insira o mes de hoje: ");
mes=scanner .nextInt();
System.out.println("insira o ano de hoje: ");
ano=scanner .nextInt();

}
public static void dia (){
if(dia!=0){
System.out.println("Hoje é dia: " +dia);
}
else{
System.out.println("dia ainda não definido");
}
}
public static void mes (){
if(mes!=0){
System.out.println("Hoje estamos no mês: "+mes);
}else{
System.out.println("mes ainda não foi definido");

}
}
public static void ano (){
if(ano!=0){


System.out.println("Hoje estamos no ano: "+ano);
}else{
System.out.println("o ano ainda não foi definido");
}
}
public static void tudo (){
if(ano!=0&&mes!=0&&dia!=0){
System.out.println("Hoje é o dia"+dia+ "do mes" +mes+ "do ano"+ano);
}else{
System.out.println("o mes ainda não foi definido");
}
}

public static void main(String[] args) {



Scanner scanner = new Scanner (System.in);

while (true) {

System.out.println("\n1) Adicionar nova data");
System.out.println("2) Adicionar novo mês");
System.out.println("3) Adicionar novo ano");
System.out.println("4) Consultar dia");
System.out.println("5) Consultar mês");
System.out.println("6) Consultar ano");
System.out.println("7) Consultar tudo");
System.out.println("8) Sair");
System.out.println("9) Tudo");
System.out.print("Escolha: ");

int escolha = scanner .nextInt();


switch (escolha){
case 1:
ndia();
break;
case 2:
nmes();
break;
case 3:
nano();
break;
case 4:
dia();
break;
case 5:
mes();
break;
case 6:
ano();
break;
case 7:
tudo();
break;
case 8:
System.out.println("saindo...");
System.exit(0);
break;
case 9:
ntudo();
break;

}
}
}
}