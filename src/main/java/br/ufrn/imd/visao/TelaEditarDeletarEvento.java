package br.ufrn.imd.visao;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TelaEditarDeletarEvento {

    public static void editarDeletarEvento(Scanner sc){

        try {
            for (int i = 0; i < 20; i++){
                System.out.println();
            }
            System.out.println("Selecione uma opção para continuar: ");
            System.out.println("1. Editar evento");
            System.out.println("2. Deletar evento");
            System.out.println("3. Voltar para a tela anterior");
            System.out.print("Digite: ");

            int opcao = sc.nextInt();

            switch (opcao){
                case 1:
                    editarEvento(sc);
                    break;
                case 2:
                    deletarEvento(sc);
                    break;
                case 3:
                    TelaGerenciarEventos.gerenciarEventos(sc);
                    break;
                default:
                    System.out.println("Opção inválida");
                    editarDeletarEvento(sc);
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Opção inválida");
            sc.nextLine();
            editarDeletarEvento(sc);
        } finally {

        }

    }

    public static void editarEvento(Scanner sc){
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("Tela de editar evento");
        System.out.println("----------------------------------------");
        editarDeletarEvento(sc);
    }

    public static void deletarEvento(Scanner sc){
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("Tela de deletar evento");
        System.out.println("----------------------------------------");
        editarDeletarEvento(sc);
    }

}
