import java.util.Scanner;

public class Main {

private static boolean lamp = false;


public static void ligar () {
lamp = true;
System.out.println("a lampada está ligada\n");
}

public static void desligar () {
lamp=false;
System.out.println("a lampada esta desligada\n");
}

public static void checar () {
if(lamp){
System.out.println("A lampada esta ligada\n");
}else{
System.out.println("A lampada esta desligada\n");
}

}

public static void main(String[] args) {

Scanner scanner = new Scanner (System.in);
while(true){
System.out.println("1: Ligar\n2: Desligar\n3: Checar:\nEscolha: ");

int escolha = scanner .nextInt();


switch (escolha) {
case 1:
ligar();
break;

case 2:
desligar();
break;

case 3:
checar();
break;

default:
System.out.println("Opção invalida, tente novamente");



}
}
}
}