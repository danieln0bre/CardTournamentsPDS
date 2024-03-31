package br.ufrn.imd.visao;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TelaPareamentoRegistrarResultados {
    public static void parearRegistrarResultados(Scanner sc){

        try {
            for (int i = 0; i < 20; i++){
                System.out.println();
            }
            System.out.println("Selecione uma opção para continuar: ");
            System.out.println("1. Parear resultado");
            System.out.println("2. Registrar resultado");
            System.out.println("3. Voltar para a tela anterior");
            System.out.print("Digite: ");

            int opcao = sc.nextInt();

            switch (opcao){
                case 1:
                    parearResultado(sc);
                    break;
                case 2:
                    registrarResultado(sc);
                    break;
                case 3:
                    TelaGerenciarEventos.gerenciarEventos(sc);
                    break;
                default:
                    System.out.println("Opção inválida");
                    parearRegistrarResultados(sc);
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Opção inválida");
            sc.nextLine();
            parearRegistrarResultados(sc);
        }

    }

    public static void registrarResultado(Scanner sc){
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("Tela de registrar resultado");
        System.out.println("----------------------------------------");
        parearRegistrarResultados(sc);
    }

    public static void parearResultado(Scanner sc){
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("Tela de parear resultado");
        System.out.println("----------------------------------------");
        parearRegistrarResultados(sc);
    }

}
